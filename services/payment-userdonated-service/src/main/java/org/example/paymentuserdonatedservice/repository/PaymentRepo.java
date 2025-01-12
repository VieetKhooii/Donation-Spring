package org.example.paymentuserdonatedservice.repository;

import org.example.paymentuserdonatedservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer> {
}
