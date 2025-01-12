package org.example.userroleservice.repository;

import org.example.userroleservice.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BlacklistedTokenRepo extends JpaRepository<BlacklistedToken, Long> {

    boolean existsByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM BlacklistedToken b WHERE b.expirationTime < CURRENT_TIMESTAMP")
    void deleteExpiredTokens();
}