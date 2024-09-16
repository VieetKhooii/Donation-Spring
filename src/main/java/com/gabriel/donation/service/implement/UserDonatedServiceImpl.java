package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.UserDonatedDTO;
import com.gabriel.donation.entity.DonationPost;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.entity.UserDonated;
import com.gabriel.donation.mapper.UserDonatedMapper;
import com.gabriel.donation.repository.DonationPostRepo;
import com.gabriel.donation.repository.UserDonatedRepo;
import com.gabriel.donation.repository.UserRepo;
import com.gabriel.donation.service.UserDonatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDonatedServiceImpl implements UserDonatedService {

    @Autowired
    UserDonatedRepo userDonatedRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    DonationPostRepo donationPostRepo;

    @Override
    public List<UserDonatedDTO> getAll()
    {
        List<UserDonated> UserDonatedList = userDonatedRepo.findAll();
        return  UserDonatedList.stream()
                .map(UserDonatedMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public UserDonatedDTO addUserDonated(UserDonatedDTO userDonatedDTO)
    {
        UserDonated userDonated = UserDonatedMapper.INSTANCE.toEntity(userDonatedDTO);
        UserDonated savedUserDonated = userDonatedRepo.save(userDonated);
        return UserDonatedMapper.INSTANCE.toDto(savedUserDonated);
    }

    @Override
    public UserDonatedDTO updateUserDonated(UserDonatedDTO userDonatedDTO, int id)
    {
        UserDonated userDonated = userDonatedRepo.findById(id).get();
        userDonated.setAmount(userDonatedDTO.getAmount());
        userDonated.setAnonymous(userDonatedDTO.isAnonymous());
        User use1=userRepo.findById(userDonatedDTO.getUserId()).get();
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
}
