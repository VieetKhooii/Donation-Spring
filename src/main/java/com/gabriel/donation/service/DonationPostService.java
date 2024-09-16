package com.gabriel.donation.service;

import com.gabriel.donation.dto.DonationPostDTO;

import java.util.List;

public interface DonationPostService {
    List<DonationPostDTO> getAll();

    DonationPostDTO addDonationPost(DonationPostDTO donationPostDTO);

    DonationPostDTO updateDonationPost(DonationPostDTO donationPostDTO, int id);

    void deleteDonationPost(int id);
}
