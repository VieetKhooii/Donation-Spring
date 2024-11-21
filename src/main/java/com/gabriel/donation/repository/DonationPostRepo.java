package com.gabriel.donation.repository;

import com.gabriel.donation.entity.DonationPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

@Repository
public interface DonationPostRepo extends JpaRepository<DonationPost, Integer> {
    @Query("SELECT SUM(ud.amount) FROM UserDonated ud WHERE ud.donationPost.id = :donationPostId AND ud.isDeleted = false")
    Long getTotalAmountForDonationPost(@Param("donationPostId") Integer donationPostId);
    @Transactional
    @Modifying
    @Query("UPDATE DonationPost dp SET dp.currentAmount = :currentAmount WHERE dp.id = :donationPostId")
    void updateCurrentAmount(@Param("donationPostId") Integer donationPostId, @Param("currentAmount") Long currentAmount);
    @Modifying
    @Transactional
    @Query("UPDATE DonationPost dp SET dp.isDeleted = true WHERE dp.id = ?1")
    void markAsDeleted(Integer id);
    Page<DonationPost> findByIsDeletedFalse(Pageable pageable);
}
