package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.PaymentDTO;
import com.gabriel.donation.dto.UserDonatedDTO;
import com.gabriel.donation.entity.DonationPost;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.entity.UserDonated;
import com.gabriel.donation.mapper.PaymentMapper;
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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
        List<UserDonated> UserDonatedList = userDonatedRepo.findAll();
        List<UserDonatedDTO> userDonatedDTOS = UserDonatedList
                .stream()
                .map(UserDonatedMapper.INSTANCE::toDto)
                .toList();
        return new PageImpl<UserDonatedDTO>(
                userDonatedDTOS,
                userDonatedRepo.findAll(pageRequest).getPageable(),
                userDonatedRepo.findAll(pageRequest).getTotalElements()
        );
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
}
