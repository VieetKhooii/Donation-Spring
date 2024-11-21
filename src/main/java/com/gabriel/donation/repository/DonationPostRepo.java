package com.gabriel.donation.repository;

import com.gabriel.donation.entity.DonationPost;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

@Repository
public interface DonationPostRepo extends JpaRepository<DonationPost, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE DonationPost dp SET dp.isDeleted = true WHERE dp.id = ?1")
    void markAsDeleted(Integer id);
    Page<DonationPost> findByIsDeletedFalse(Pageable pageable);
}
