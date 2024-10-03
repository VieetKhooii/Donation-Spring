package com.gabriel.donation.service;

import com.gabriel.donation.dto.PaymentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PaymentService {
    Page<PaymentDTO> getAll(PageRequest pageRequest);


    PaymentDTO addPayment(PaymentDTO paymentDTO);

    PaymentDTO updatePayment(PaymentDTO paymentDTO, int id);

    void deletePayment(int id);

    PaymentDTO getPaymentById(int id);
}
