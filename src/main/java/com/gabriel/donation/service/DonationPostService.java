package com.gabriel.donation.service;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.ImageOfDonationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DonationPostService {
    Page<DonationPostDTO> getAll(PageRequest pageRequest);

//    DonationPostDTO addDonationPost(DonationPostDTO donationPostDTO);

    @Transactional
    DonationPostDTO addDonationPost(DonationPostDTO donationPostDTO, ImageOfDonationDTO imagePostDTO);

    DonationPostDTO updateDonationPost(DonationPostDTO donationPostDTO, int id);
    DonationPostDTO updateDonationPostAndImage(DonationPostDTO donationPostDTO,ImageOfDonationDTO imageOfDonationDTO, int id);
    void deleteDonationPost(int id);

    DonationPostDTO getDonationPostById(int id);

    DonationPostDTO findById(int id);
}
