package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.entity.Category;
import com.gabriel.donation.entity.DonationPost;
import com.gabriel.donation.entity.Sponsor;
import com.gabriel.donation.mapper.DonationPostMapper;
import com.gabriel.donation.repository.CategoryRepo;
import com.gabriel.donation.repository.DonationPostRepo;
import com.gabriel.donation.repository.SponsorRepo;
import com.gabriel.donation.service.DonationPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationPostServiceImpl implements DonationPostService {

    @Autowired
    DonationPostRepo donationPostRepo;
    @Autowired
    SponsorRepo sponsorRepo;
    @Autowired
    CategoryRepo categoryRepo;

    @Override
    public List<DonationPostDTO> getAll()
    {
        List<DonationPost> DonationPosts = donationPostRepo.findAll();
        return DonationPosts.stream()
                .map(DonationPostMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public DonationPostDTO addDonationPost(DonationPostDTO donationPostDTO)
    {
        DonationPost donationPost=DonationPostMapper.INSTANCE.toEntity(donationPostDTO);
        DonationPost savedDonationPost=donationPostRepo.save(donationPost);
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
        Sponsor sponsor1=sponsorRepo.findById(donationPostDTO.getSponsorId()).get();
        Category category1=categoryRepo.findById(donationPostDTO.getCategoryId()).get();
        donationPost.setSponsor(sponsor1);
        donationPost.setCategory(category1);

        DonationPost updatedDonationPost=donationPostRepo.save(donationPost);
        return DonationPostMapper.INSTANCE.toDto(updatedDonationPost);
    }

    @Override
    public void deleteDonationPost(int id)
    {
        if(donationPostRepo.existsById(id))
            donationPostRepo.deleteById(id);
    }
}
