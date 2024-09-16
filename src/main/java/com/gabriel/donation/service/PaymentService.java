package com.gabriel.donation.service;

import com.gabriel.donation.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    List<PaymentDTO> getAll();

    PaymentDTO addPayment(PaymentDTO paymentDTO);

    PaymentDTO updatePayment(PaymentDTO paymentDTO, int id);

    void deletePayment(int id);
}
