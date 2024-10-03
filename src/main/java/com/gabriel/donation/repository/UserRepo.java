package com.gabriel.donation.repository;

import com.gabriel.donation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByPhone(String phone);
    boolean existsByPhone(String phone);
}
