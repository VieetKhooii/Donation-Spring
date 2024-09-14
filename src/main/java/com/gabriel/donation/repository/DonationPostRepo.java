package com.gabriel.donation.repository;

import com.gabriel.donation.entity.DonationPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationPostRepo extends JpaRepository<DonationPost, Integer> {
}
