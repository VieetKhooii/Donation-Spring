package com.gabriel.donation.service;

import com.gabriel.donation.dto.ImageOfDonationDTO;

import java.util.List;

public interface ImageOfDonationService {
    List<ImageOfDonationDTO> getAll();

    ImageOfDonationDTO addImageOfDonation(ImageOfDonationDTO imageOfDonationDTO);

    ImageOfDonationDTO updateImageOfDonation(ImageOfDonationDTO imageOfDonationDTO, int id);

    void deleteImageOfDonation(int id);
}
