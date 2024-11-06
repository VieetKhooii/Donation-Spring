package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.UserDonatedDTO;
import com.gabriel.donation.entity.DonationPost;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.entity.UserDonated;
import com.gabriel.donation.mapper.UserDonatedMapper;
import com.gabriel.donation.repository.DonationPostRepo;
import com.gabriel.donation.repository.UserDonatedRepo;
import com.gabriel.donation.repository.UserRepo;
import com.gabriel.donation.service.DonationPostService;
import com.gabriel.donation.service.UserDonatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserDonatedServiceImpl implements UserDonatedService {

    @Autowired
    UserDonatedRepo userDonatedRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    DonationPostRepo donationPostRepo;
    @Autowired
    DonationPostService donationPostService;

    @Override
    public Page<UserDonatedDTO> getAll(PageRequest pageRequest)
    {
        List<UserDonated> UserDonatedList = userDonatedRepo.findAll(pageRequest).getContent();
        List<UserDonatedDTO> userDonatedDTOS = UserDonatedList
                .stream()
                .map(UserDonatedMapper.INSTANCE::toDto)
                .toList();
        return new PageImpl<UserDonatedDTO>(
                userDonatedDTOS,
                pageRequest,
                userDonatedRepo.findAll(pageRequest).getTotalElements()
        );
    }

    @Override
    public Page<UserDonatedDTO> getPageByPostId(PageRequest pageRequest, int postId) {
        DonationPost donationPost = donationPostRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy DonationPost với id: " + postId));

        Page<UserDonated> userDonatedPage = userDonatedRepo.findByDonationPost(donationPost, pageRequest);
        List<UserDonatedDTO> userDonatedDTOList = userDonatedPage
                .getContent()
                .stream()
                .map(UserDonatedMapper.INSTANCE::toDto)
                .toList();
        return new PageImpl<>(userDonatedDTOList, pageRequest, userDonatedPage.getTotalElements());
    }

    @Override
    public UserDonatedDTO addUserDonated(UserDonatedDTO userDonatedDTO)
    {
        UserDonated userDonated = UserDonatedMapper.INSTANCE.toEntity(userDonatedDTO);
        System.out.println(userDonated.getPaymentMethod());
        UserDonated savedUserDonated = userDonatedRepo.save(userDonated);
        return UserDonatedMapper.INSTANCE.toDto(savedUserDonated);
    }

    @Override
    public UserDonatedDTO updateUserDonated(UserDonatedDTO userDonatedDTO, int id)
    {
        UserDonated userDonated = userDonatedRepo.findById(id).get();
        userDonated.setAmount(userDonatedDTO.getAmount());
        userDonated.setAnonymous(userDonatedDTO.isAnonymous());
        User use1=userRepo.findById(userDonatedDTO.getUserId());
        DonationPost donate1=donationPostRepo.findById(userDonatedDTO.getDonationPostId()).get();
        userDonated.setUser(use1);
        userDonated.setDonationPost(donate1);
        userDonated.setDonateDate(userDonatedDTO.getDonateDate());
        userDonated.setDeleted(userDonatedDTO.isDeleted());
        UserDonated updatedUserDonated = userDonatedRepo.save(userDonated);
        return UserDonatedMapper.INSTANCE.toDto(updatedUserDonated);

    }

    @Override
    public void deleteUserDonated(int id)
    {
        if(userDonatedRepo.existsById(id))
            userDonatedRepo.deleteById(id);
    }

    @Override
    public UserDonatedDTO getUserDonatedById(int id)
    {
        return UserDonatedMapper.INSTANCE.toDto(userDonatedRepo.findById(id).get());
    }

    @Override
    public void processDonation(UserDonatedDTO userDonatedDTO, int donatePersonId) {
        userDonatedDTO.setUserId(donatePersonId);
        addUserDonated(userDonatedDTO);

        DonationPostDTO donationPostDTO = donationPostService.findById(userDonatedDTO.getDonationPostId());
        donationPostDTO.setCurrentAmount(donationPostDTO.getCurrentAmount() + userDonatedDTO.getAmount());
        donationPostDTO.setNumberOfDonation(donationPostDTO.getNumberOfDonation() + 1);
        donationPostService.updateDonationPost(donationPostDTO, userDonatedDTO.getDonationPostId());
    }

    //Tìm list userdonated của từng post,sắp xếp desc theo amount và thêm vào 1 list bự ở ngoài
    @Override
    public List<UserDonatedDTO> rankingUserDonatedByAmount() {
        List<UserDonatedDTO> allUserDonatedDTOs = new ArrayList<>();

        List<DonationPost> donationPostList = donationPostRepo.findAll();
        List<UserDonated> userDonatedList = userDonatedRepo.findAll();

        for (DonationPost donationPost : donationPostList) {
            System.out.println("ID of post: " + donationPost.getId());

            // Lấy list userDonated của từng donationPost và filter giảm dần theo amount(chưa có giới hạn top 10 nha)
            List<UserDonated> filteredUserDonatedList = userDonatedList.stream()
                    .filter(userDonated -> userDonated.getDonationPost().getId() == donationPost.getId())
                    .sorted((u1, u2) -> Long.compare(u2.getAmount(), u1.getAmount()))
                    .toList();

            // chuyển dto rồi add từng list con vaào list bự
            List<UserDonatedDTO> userDonatedDTOs = filteredUserDonatedList.stream()
                    .map(UserDonatedMapper.INSTANCE::toDto)
                    .toList();

            allUserDonatedDTOs.addAll(userDonatedDTOs);
            // số lượng người đã đóng góp mỗi post
//            System.out.println("Number of donations for post ID " + donationPost.getId() + " is: " + userDonatedDTOs.size());
        }
        return allUserDonatedDTOs;
    }

    //Tìm userDonated nhiều nhất mỗi tháng
    @Override
    public List<UserDonatedDTO> rankingUserDonatedByMonth(int presentYear) {
        List<UserDonatedDTO> allUserDonatedDTOs = new ArrayList<>();
        List<UserDonated> userDonatedList = userDonatedRepo.findAll();

        Map<Integer, UserDonatedDTO> monthlyTopDonors = new HashMap<>();

        for (UserDonated userDonated : userDonatedList) {
            int year = userDonated.getDonateDate().getYear() + 1900;//hàm getyear trả về số năm -1900
            int month = userDonated.getDonateDate().getMonth() + 1;//getMonth trả về 0

            if (year == presentYear) {
                //nếu tháng đó duyệt lần nào chưa, chưa có thì max bắt đầu =0, ngược lại lấy amount ra so sánh với max trước đó
                long max = monthlyTopDonors.containsKey(month) ? monthlyTopDonors.get(month).getAmount() : 0;

                if (max < userDonated.getAmount() ) {
                    UserDonatedDTO topUserForMonth = UserDonatedMapper.INSTANCE.toDto(userDonated);
                    monthlyTopDonors.put(month, topUserForMonth); // Update top donate for the month
//                    System.out.println("Max donation for month " + month + " is: " + userDonated.getAmount());
                }
            }
        }
        allUserDonatedDTOs.addAll(monthlyTopDonors.values());
        return allUserDonatedDTOs;
    }

    //Tìm post được donate nhiều nhất mỗi tháng
    @Override
    public List<UserDonatedDTO> rankingDonationPostAmountByMonth(int presentYear) {
        List<UserDonatedDTO> allUserDonatedDTOs = new ArrayList<>();
        List<UserDonated> userDonatedList = userDonatedRepo.findAll();

        // Map để lưu tổng số tiền donate cho mỗi DonationPost theo tháng
        Map<Integer, Map<Integer, Long>> monthlyDonationSums = new HashMap<>();

        // Tính tổng số tiền donate cho mỗi DonationPost theo tháng
        for (UserDonated userDonated : userDonatedList) {
            int year = userDonated.getDonateDate().getYear() + 1900; // getYear trả về số năm -1900
            int month = userDonated.getDonateDate().getMonth() + 1; // getMonth trả về 0

            if (year == presentYear) {
                int donationPostId = userDonated.getDonationPost().getId();
                long amount = userDonated.getAmount();

                // Tạo map cho tháng nếu chưa tồn tại
                monthlyDonationSums.putIfAbsent(month, new HashMap<>());

                // Cộng dồn số tiền cho DonationPost trong tháng
                Map<Integer, Long> donationPostSums = monthlyDonationSums.get(month);
                donationPostSums.put(donationPostId, donationPostSums.getOrDefault(donationPostId, 0L) + amount);
            }
        }

        // Tìm DonationPost có tổng số tiền donate cao nhất mỗi tháng
        Map<Integer, UserDonatedDTO> monthlyTopDonors = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, Long>> monthlyEntry : monthlyDonationSums.entrySet()) {
            int month = monthlyEntry.getKey();
            Map<Integer, Long> donationPostSums = monthlyEntry.getValue();

            // Tìm DonationPost có tổng số tiền donate cao nhất
            long maxAmount = 0;
            int topDonationPostId = 0;
            for (Map.Entry<Integer, Long> donationEntry : donationPostSums.entrySet()) {
                if (donationEntry.getValue() > maxAmount) {
                    maxAmount = donationEntry.getValue();
                    topDonationPostId = donationEntry.getKey();
                }
            }

            // Nếu tìm thấy DonationPost có số tiền donate cao nhất, chuyển đổi sang DTO
            if (topDonationPostId != 0) {
                int finalTopDonationPostId = topDonationPostId;
                UserDonated topUserDonated = userDonatedList.stream()
                        .filter(u -> u.getDonationPost().getId() == finalTopDonationPostId &&
                                u.getDonateDate().getMonth() + 1 == month &&
                                u.getDonateDate().getYear() + 1900 == presentYear)
                        .findFirst()
                        .orElse(null);

                if (topUserDonated != null) {
                    UserDonatedDTO topUserForMonth = UserDonatedMapper.INSTANCE.toDto(topUserDonated);
                    topUserForMonth.setAmount(maxAmount); // Gán tổng số tiền donate
                    monthlyTopDonors.put(month, topUserForMonth);
                    System.out.println("Max donation for month " + month + " is: " + maxAmount);
                }
            }
        }

        allUserDonatedDTOs.addAll(monthlyTopDonors.values());
        return allUserDonatedDTOs;
    }

    //Tìm post được nhiều người donate nhất mỗi tháng
    @Override
    public List<UserDonatedDTO> countUserDonatedByPost(int presentYear) {
        List<UserDonatedDTO> allUserDonatedDTOs = new ArrayList<>();
        List<UserDonated> userDonatedList = userDonatedRepo.findAll();

        // Map để lưu số lần quyên góp cho mỗi DonationPost theo tháng
        Map<Integer, Map<Integer, Integer>> monthlyDonationCounts = new HashMap<>();

        // Đếm số lần xuất hiện của mỗi DonationPost theo tháng
        for (UserDonated userDonated : userDonatedList) {
            int year = userDonated.getDonateDate().getYear() + 1900; // getYear trả về số năm -1900
            int month = userDonated.getDonateDate().getMonth() + 1; // getMonth trả về 0

            if (year == presentYear) {
                int donationPostId = userDonated.getDonationPost().getId();

                // Tạo map cho tháng nếu chưa tồn tại
                monthlyDonationCounts.putIfAbsent(month, new HashMap<>());

                // Tăng số lần xuất hiện cho DonationPost trong tháng
                Map<Integer, Integer> donationPostCounts = monthlyDonationCounts.get(month);
                donationPostCounts.put(donationPostId, donationPostCounts.getOrDefault(donationPostId, 0) + 1);
            }
        }

        // Tìm DonationPost có số lần quyên góp nhiều nhất mỗi tháng
        Map<Integer, UserDonatedDTO> monthlyTopDonors = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, Integer>> monthlyEntry : monthlyDonationCounts.entrySet()) {
            int month = monthlyEntry.getKey();
            Map<Integer, Integer> donationPostCounts = monthlyEntry.getValue();

            // Tìm DonationPost có số lần quyên góp cao nhất
            int maxCount = 0;
            int topDonationPostId = 0;
            for (Map.Entry<Integer, Integer> donationEntry : donationPostCounts.entrySet()) {
                if (donationEntry.getValue() > maxCount) {
                    maxCount = donationEntry.getValue();
                    topDonationPostId = donationEntry.getKey();
                }
            }

            // Nếu tìm thấy DonationPost có số lần quyên góp cao nhất, chuyển đổi sang DTO
            if (topDonationPostId != 0) {
                int finalTopDonationPostId = topDonationPostId;
                UserDonated topUserDonated = userDonatedList.stream()
                        .filter(u -> u.getDonationPost().getId() == finalTopDonationPostId &&
                                u.getDonateDate().getMonth() + 1 == month &&
                                u.getDonateDate().getYear() + 1900 == presentYear)
                        .findFirst()
                        .orElse(null);

                if (topUserDonated != null) {
                    UserDonatedDTO topUserForMonth = UserDonatedMapper.INSTANCE.toDto(topUserDonated);
                    topUserForMonth.setAmount(maxCount); // Gán số lần quyên góp
                    monthlyTopDonors.put(month, topUserForMonth);
                    System.out.println("Max donation count for month " + month + " is: " + maxCount);
                }
            }
        }

        allUserDonatedDTOs.addAll(monthlyTopDonors.values());
        return allUserDonatedDTOs;
    }


//xin đừng care
//    @Override
//    public List<UserDonatedDTO> rankingUserDonatedByAmount() {
//        List<UserDonatedDTO> allUserDonatedDTOs = new ArrayList<>();
//
//        List<DonationPost> donationPostList = donationPostRepo.findAll();
//        List<UserDonated> userDonatedList = userDonatedRepo.findAll();
//        long max=0;
//
//        for (DonationPost donationPost : donationPostList) {
//
//            System.out.println("ID of post: " + donationPost.getId());
//
//            UserDonatedDTO userDonatedDTO=new UserDonatedDTO();
//            for(UserDonated userDonated : userDonatedList)
//            {
//                if(userDonated.getDonationPost().getId() == donationPost.getId())
//                {
//                    if(max<userDonated.getAmount())
//                    {
//                        max=userDonated.getAmount();
//                        userDonatedDTO = UserDonatedMapper.INSTANCE.toDto(userDonated);
//                    }
//                }
//            }
//            if (userDonatedDTO != null) {
//                allUserDonatedDTOs.add(userDonatedDTO);
//                System.out.println("Max donation for post ID " + donationPost.getId() + " is: " + max);
//            }
//        }
//        return allUserDonatedDTOs;
//    }

}
