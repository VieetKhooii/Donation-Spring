package com.gabriel.donation.repository;

import com.gabriel.donation.entity.UserDonated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDonatedRepo extends JpaRepository<UserDonated, Integer> {
}
