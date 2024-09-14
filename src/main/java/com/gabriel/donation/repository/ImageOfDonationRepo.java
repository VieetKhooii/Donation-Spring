package com.gabriel.donation.repository;

import com.gabriel.donation.entity.ImageOfDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageOfDonationRepo extends JpaRepository<ImageOfDonation, Integer> {
}
