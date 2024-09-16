package com.gabriel.donation.mapper;

import com.gabriel.donation.dto.PaymentDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.Payment;
import com.gabriel.donation.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    //               dto's id    entity's id
    @Mapping(source = "paymentId", target = "id")
    Payment toEntity(PaymentDTO paymentDTO);

    @Mapping(source = "id", target = "paymentId")
    @Mapping(source = "user.id", target = "userId")
    // Fields from entity that are Object type (Foreign key) must have ".id" (ex: role.id)
    // so that DTO can understand and store it
    PaymentDTO toDto(Payment payment);
}
