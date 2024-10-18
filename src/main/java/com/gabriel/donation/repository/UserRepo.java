package com.gabriel.donation.repository;

import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByPhone(String phone);
    boolean existsByPhone(String phone);
    User findById(int id);
    Optional<User> findByEmail(String email);
}