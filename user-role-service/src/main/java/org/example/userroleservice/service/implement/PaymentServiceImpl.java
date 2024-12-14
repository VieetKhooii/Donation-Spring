package org.example.userroleservice.service.implement;

import com.gabriel.donation.dto.PaymentDTO;
import com.gabriel.donation.entity.Payment;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.mapper.PaymentMapper;
import com.gabriel.donation.repository.PaymentRepo;
import com.gabriel.donation.repository.UserRepo;
import com.gabriel.donation.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentRepo paymentRepo;
    @Autowired
    UserRepo userRepo;

    @Override
    public Page<PaymentDTO> getAll(PageRequest pageRequest)
    {
        List<Payment> payments = paymentRepo.findAll(pageRequest).getContent();
        List<PaymentDTO> paymentDTOS = payments
                .stream()
                .map(PaymentMapper.INSTANCE::toDto)
                .toList();
        return new PageImpl<PaymentDTO>(
                paymentDTOS,
                pageRequest,
                paymentRepo.findAll(pageRequest).getTotalElements()
        );
    }

    @Override
    public PaymentDTO addPayment(PaymentDTO paymentDTO)
    {
        System.out.println("Transaction Date in DTO: " + paymentDTO.getTransactionDate());
        Payment payment = PaymentMapper.INSTANCE.toEntity(paymentDTO);
        System.out.println("Transaction Date in Entity: " + payment.getTransaction_date());
        Payment savedPayment = paymentRepo.save(payment);
        return PaymentMapper.INSTANCE.toDto(savedPayment);
    }

    @Override
    public PaymentDTO updatePayment(PaymentDTO paymentDTO, int id)
    {
        Payment payment = paymentRepo.findById(id).get();
        payment.setAmount(paymentDTO.getAmount());
        payment.setTransaction_date(paymentDTO.getTransactionDate());
        payment.setDeleted(paymentDTO.isDeleted());
        User use1=userRepo.findById(paymentDTO.getUserId());
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

    @Override
    public PaymentDTO getPaymentById(int id)
    {
        return PaymentMapper.INSTANCE.toDto(paymentRepo.findById(id).get());
    }

}
