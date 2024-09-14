package com.gabriel.donation.repository;

import com.gabriel.donation.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorRepo extends JpaRepository<Sponsor, Integer> {
}
