package org.example.paymentuserdonatedservice.repository;

import org.example.paymentuserdonatedservice.entity.UserDonated;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDonatedRepo extends JpaRepository<UserDonated, Integer> {
    @Query("SELECT u.userId, u.anonymous as anonymous, SUM(u.amount) as amount " +
            "FROM UserDonated u " +
            "WHERE u.donationPostId = :donationPostId " +
            "GROUP BY u.userId, u.anonymous " +
            "ORDER BY SUM(u.amount) DESC")
    List<UserDonated.UserDonatedProjection> findByDonationPost(int donationPostId, Pageable pageable);
}
