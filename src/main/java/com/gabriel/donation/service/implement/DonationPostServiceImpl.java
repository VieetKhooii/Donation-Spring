package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.ImageOfDonationDTO;
import com.gabriel.donation.entity.Category;
import com.gabriel.donation.entity.DonationPost;
import com.gabriel.donation.entity.ImageOfDonation;
import com.gabriel.donation.mapper.DonationPostMapper;
import com.gabriel.donation.mapper.ImageOfDonationMapper;
import com.gabriel.donation.repository.CategoryRepo;
import com.gabriel.donation.repository.DonationPostRepo;
import com.gabriel.donation.repository.ImageOfDonationRepo;
import com.gabriel.donation.service.DonationPostService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class DonationPostServiceImpl implements DonationPostService {

    @Autowired
    DonationPostRepo donationPostRepo;
    @Autowired
    ImageOfDonationRepo imageOfDonationRepo;
    @Autowired
    CategoryRepo categoryRepo;

    @Override
    public Page<DonationPostDTO> getAll(PageRequest pageRequest) {
        // Lấy danh sách DonationPosts không bị xóa
        Page<DonationPost> donationPostsPage = donationPostRepo.findByIsDeletedFalse(pageRequest);

        // Chuyển đổi danh sách DonationPost sang DonationPostDTO
        List<DonationPostDTO> donationPostDTOS = donationPostsPage.getContent()
                .stream()
                .filter(donationPost -> donationPost.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(LocalDate.now()))
                .map(post -> {
                    List<ImageOfDonation> imagesOfDonationPosts = imageOfDonationRepo.findByDonationPostId(post.getId());
                    DonationPostDTO donationPostDTO = DonationPostMapper.INSTANCE.toDto(post);
                    List<ImageOfDonationDTO> imageOfDonationDTOS = imagesOfDonationPosts
                            .stream()
                            .map(ImageOfDonationMapper.INSTANCE::toDto)
                            .toList();
                    donationPostDTO.setLstImages(imageOfDonationDTOS);
                    return donationPostDTO;
                })
                .toList();

        // Trả về một PageImpl chứa danh sách DTO, pageable và tổng số phần tử
        return new PageImpl<>(
                donationPostDTOS,
                donationPostsPage.getPageable(),
                donationPostsPage.getTotalElements()
        );
    }



    @Transactional
    @Override
    public DonationPostDTO addDonationPost(DonationPostDTO donationPostDTO, ImageOfDonationDTO imagePostDTO) {
        // Chuyển đổi DTO thành entity cho DonationPost
        DonationPost donationPost = DonationPostMapper.INSTANCE.toEntity(donationPostDTO);

        // Lưu DonationPost vào cơ sở dữ liệu
        DonationPost savedDonationPost = donationPostRepo.save(donationPost);
        // Chuyển đổi DTO thành entity cho ImageOfDonation
        ImageOfDonation imagePost = new ImageOfDonation();
        imagePost.setDescription(imagePostDTO.getDescription());

        imagePost.setUrl(imagePostDTO.getUrl());
        imagePost.setDonationPost(savedDonationPost);
        // Lưu ImageOfDonation vào cơ sở dữ liệu
        imageOfDonationRepo.save(imagePost);

        // Trả về DTO của DonationPost đã lưu
        return DonationPostMapper.INSTANCE.toDto(savedDonationPost);
    }

    @Override
    public DonationPostDTO updateDonationPost(DonationPostDTO donationPostDTO, int id)
    {
        DonationPost donationPost=donationPostRepo.findById(id).get();
        donationPost.setCurrentAmount(donationPostDTO.getCurrentAmount());
        donationPost.setDeleted(donationPostDTO.isDeleted());
        donationPost.setStartDate(donationPostDTO.getStartDate());
        donationPost.setEndDate(donationPostDTO.getEndDate());
        donationPost.setGoalAmount(donationPostDTO.getGoalAmount());
        donationPost.setStory(donationPostDTO.getStory());
        donationPost.setTitle(donationPostDTO.getTitle());
        donationPost.setNumberOfDonation(donationPostDTO.getNumberOfDonation());
//        Sponsor sponsor1=sponsorRepo.findById(donationPostDTO.getSponsorId()).get();
        Category category1=categoryRepo.findById(donationPostDTO.getCategoryId()).get();
//        donationPost.setSponsor(sponsor1);
        donationPost.setCategory(category1);

        DonationPost updatedDonationPost=donationPostRepo.save(donationPost);
        return DonationPostMapper.INSTANCE.toDto(updatedDonationPost);
    }
    @Transactional
    @Override
    public DonationPostDTO updateDonationPostAndImage(DonationPostDTO donationPostDTO,ImageOfDonationDTO imageOfDonationDTO, int id)
    {
        DonationPost donationPost=donationPostRepo.findById(id).get();
//        donationPost.setCurrentAmount(donationPostDTO.getCurrentAmount());
//        donationPost.setDeleted(donationPostDTO.isDeleted());
        donationPost.setStartDate(donationPostDTO.getStartDate());
        donationPost.setEndDate(donationPostDTO.getEndDate());
        donationPost.setGoalAmount(donationPostDTO.getGoalAmount());
        donationPost.setStory(donationPostDTO.getStory());
        donationPost.setTitle(donationPostDTO.getTitle());
//        donationPost.setNumberOfDonation(donationPostDTO.getNumberOfDonation());
//        Category category1=categoryRepo.findById(donationPostDTO.getCategoryId()).get();
//        donationPost.setCategory(category1);
        DonationPost updatedDonationPost=donationPostRepo.save(donationPost);


        ImageOfDonation imageOfDonation = imageOfDonationRepo.findByDonationPostId(donationPost.getId()).get(0);
        imageOfDonation.setDescription(imageOfDonationDTO.getDescription());
        imageOfDonation.setUrl(imageOfDonationDTO.getUrl());
        imageOfDonationRepo.save(imageOfDonation);

        return DonationPostMapper.INSTANCE.toDto(updatedDonationPost);
    }

    @Override
    public void deleteDonationPost(int id)
    {
        if(donationPostRepo.existsById(id))
        {
            imageOfDonationRepo.markAsDeleted(id);
            donationPostRepo.markAsDeleted(id);
        }
    }

    @Override
    public DonationPostDTO getDonationPostById(int id)
    {
        return donationPostRepo.findById(id)
                .map( post ->
                        {
                            List<ImageOfDonation> ImagesOfDonationPosts = imageOfDonationRepo.findByDonationPostId(post.getId());
                            DonationPostDTO donationPostDTO = DonationPostMapper.INSTANCE.toDto(post);
                            List<ImageOfDonationDTO> imageOfDonationDTOS = ImagesOfDonationPosts
                                    .stream()
                                    .map(ImageOfDonationMapper.INSTANCE::toDto)
                                    .toList();
                            donationPostDTO.setLstImages(imageOfDonationDTOS);
                            return donationPostDTO;
                        })
                .orElseThrow(() -> new EntityNotFoundException("Donation post not found with id: " + id));
    }

    @Override
    public DonationPostDTO findById(int id){
        return  DonationPostMapper.INSTANCE.toDto(donationPostRepo.findById(id).get());
    }
}
