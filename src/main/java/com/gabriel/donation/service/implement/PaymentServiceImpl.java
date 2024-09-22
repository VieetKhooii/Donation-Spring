package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.PaymentDTO;
import com.gabriel.donation.entity.Payment;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.mapper.PaymentMapper;
import com.gabriel.donation.repository.PaymentRepo;
import com.gabriel.donation.repository.UserRepo;
import com.gabriel.donation.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentRepo paymentRepo;
    @Autowired
    UserRepo userRepo;

    @Override
    public List<PaymentDTO> getAll()
    {
        List<Payment> payments = paymentRepo.findAll();
        return  payments.stream()
                .map(PaymentMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public PaymentDTO addPayment(PaymentDTO paymentDTO)
    {
        Payment payment = PaymentMapper.INSTANCE.toEntity(paymentDTO);
        Payment savedPayment= paymentRepo.save(payment);
        return PaymentMapper.INSTANCE.toDto(savedPayment);
    }

    @Override
    public PaymentDTO updatePayment(PaymentDTO paymentDTO, int id)
    {
        Payment payment = paymentRepo.findById(id).get();
        payment.setAmount(paymentDTO.getAmount());
        payment.setTransaction_date(paymentDTO.getTransactionDate());
        payment.setDeleted(paymentDTO.isDeleted());
        User use1=userRepo.findById(paymentDTO.getUserId()).get();
        payment.setUser(use1);
        Payment updatedPayment= paymentRepo.save(payment);
        return PaymentMapper.INSTANCE.toDto(updatedPayment);
    }

    @Override
    public void deletePayment(int id)
    {
        if(paymentRepo.existsById(id))
            paymentRepo.deleteById(id);
    }

}
