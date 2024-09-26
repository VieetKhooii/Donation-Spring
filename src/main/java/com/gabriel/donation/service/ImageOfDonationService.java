package com.gabriel.donation.service;

import com.gabriel.donation.dto.ImageOfDonationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ImageOfDonationService {
    Page<ImageOfDonationDTO> getAll(PageRequest pageRequest);

    ImageOfDonationDTO addImageOfDonation(ImageOfDonationDTO imageOfDonationDTO);

    ImageOfDonationDTO updateImageOfDonation(ImageOfDonationDTO imageOfDonationDTO, int id);

    void deleteImageOfDonation(int id);

    ImageOfDonationDTO getImageOfDonationById(int id);
}
