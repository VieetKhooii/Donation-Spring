package org.example.paymentuserdonatedservice.service;

import org.example.paymentuserdonatedservice.dto.PaymentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface PaymentService {
    Page<PaymentDTO> getAll(PageRequest pageRequest);


    PaymentDTO addPayment(PaymentDTO paymentDTO);

    PaymentDTO updatePayment(PaymentDTO paymentDTO, int id);

    void deletePayment(int id);

    PaymentDTO getPaymentById(int id);
}
