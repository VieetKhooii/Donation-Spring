package com.gabriel.donation.service;

import com.gabriel.donation.dto.DonationPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface DonationPostService {
    Page<DonationPostDTO> getAll(PageRequest pageRequest);

    DonationPostDTO addDonationPost(DonationPostDTO donationPostDTO);

    DonationPostDTO updateDonationPost(DonationPostDTO donationPostDTO, int id);

    void deleteDonationPost(int id);

    DonationPostDTO getDonationPostById(int id);

    DonationPostDTO findById(int id);
}
