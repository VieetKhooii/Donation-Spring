package org.example.paymentuserdonatedservice.mapper;

import com.gabriel.donation.dto.PaymentDTO;
import com.gabriel.donation.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    //               dto's id    entity's id
    @Mapping(source = "paymentId", target = "id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "transactionDate", target = "transaction_date")
    Payment toEntity(PaymentDTO paymentDTO);

    @Mapping(source = "id", target = "paymentId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "transaction_date", target = "transactionDate")
    // Fields from entity that are Object type (Foreign key) must have ".id" (ex: role.id)
    // so that DTO can understand and store it
    PaymentDTO toDto(Payment payment);
}
