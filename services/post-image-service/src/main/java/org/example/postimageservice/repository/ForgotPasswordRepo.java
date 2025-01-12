package org.example.postimageservice.repository;

import com.gabriel.donation.entity.ForgotPassword;
import com.gabriel.donation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ForgotPasswordRepo extends JpaRepository<ForgotPassword, Integer> {

    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);

    List<ForgotPassword> findAllByExpirationTimeBefore(Date currentDate);
}
