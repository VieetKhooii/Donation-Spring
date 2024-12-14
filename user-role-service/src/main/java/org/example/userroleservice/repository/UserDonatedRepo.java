package org.example.userroleservice.repository;

import com.gabriel.donation.entity.DonationPost;
import com.gabriel.donation.entity.UserDonated;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDonatedRepo extends JpaRepository<UserDonated, Integer> {
    @Query("SELECT u.user.name as username, u.anonymous as anonymous, SUM(u.amount) as amount " +
            "FROM UserDonated u " +
            "WHERE u.donationPost = :donationPost " +
            "GROUP BY u.user, u.anonymous " +
            "ORDER BY SUM(u.amount) DESC")
    List<UserDonated.UserDonatedProjection> findByDonationPost(DonationPost donationPost, Pageable pageable);
}
