package com.gabriel.donation.repository;

import com.gabriel.donation.entity.DonationPost;
import com.gabriel.donation.entity.UserDonated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDonatedRepo extends JpaRepository<UserDonated, Integer> {
    Page<UserDonated> findByDonationPost(DonationPost donationPost, Pageable pageable);
}
