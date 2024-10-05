package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.entity.Category;
import com.gabriel.donation.entity.DonationPost;
import com.gabriel.donation.mapper.DonationPostMapper;
import com.gabriel.donation.repository.CategoryRepo;
import com.gabriel.donation.repository.DonationPostRepo;
import com.gabriel.donation.service.DonationPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationPostServiceImpl implements DonationPostService {

    @Autowired
    DonationPostRepo donationPostRepo;
    @Autowired
    CategoryRepo categoryRepo;

    @Override
    public Page<DonationPostDTO> getAll(PageRequest pageRequest)
    {
        List<DonationPost> DonationPosts = donationPostRepo.findAll(pageRequest).getContent();
        List<DonationPostDTO> donationPostDTOS = DonationPosts
                .stream()
                .map(DonationPostMapper.INSTANCE::toDto)
                .toList();
        return new PageImpl<DonationPostDTO>(
                donationPostDTOS,
                donationPostRepo.findAll(pageRequest).getPageable(),
                donationPostRepo.findAll(pageRequest).getTotalElements()
        );
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
//        Sponsor sponsor1=sponsorRepo.findById(donationPostDTO.getSponsorId()).get();
        Category category1=categoryRepo.findById(donationPostDTO.getCategoryId()).get();
//        donationPost.setSponsor(sponsor1);
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

    @Override
    public DonationPostDTO getDonationPostById(int id)
    {
        return DonationPostMapper.INSTANCE.toDto(donationPostRepo.findById(id).get());
    }

    @Override
    public DonationPostDTO findById(int id){
        return  DonationPostMapper.INSTANCE.toDto(donationPostRepo.findById(id).get());
    }
}
