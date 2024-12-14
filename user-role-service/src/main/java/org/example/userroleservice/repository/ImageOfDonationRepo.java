package org.example.userroleservice.repository;

import com.gabriel.donation.entity.ImageOfDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImageOfDonationRepo extends JpaRepository<ImageOfDonation, Integer> {
    List<ImageOfDonation> findByDonationPostId(Integer donationPostID);
//    Optional<ImageOfDonation> findByDonationPostId(Integer donationPostId);
    @Modifying
    @Transactional
    @Query("UPDATE ImageOfDonation img SET img.isDeleted = true WHERE img.donationPost.id = ?1")
    void markAsDeleted(Integer id);
}
