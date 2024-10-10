package com.gabriel.donation.repository;

import com.gabriel.donation.dto.ImageOfDonationDTO;
import com.gabriel.donation.entity.ImageOfDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageOfDonationRepo extends JpaRepository<ImageOfDonation, Integer> {
    List<ImageOfDonation> findByDonationPostId(Integer donationPostID);
}
