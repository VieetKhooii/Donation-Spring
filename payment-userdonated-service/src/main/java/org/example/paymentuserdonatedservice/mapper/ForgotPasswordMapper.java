package org.example.paymentuserdonatedservice.mapper;

import com.gabriel.donation.dto.ForgotPasswordDTO;
import com.gabriel.donation.entity.ForgotPassword;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ForgotPasswordMapper {
    ForgotPasswordMapper INSTANCE = Mappers.getMapper(ForgotPasswordMapper.class);

    @Mapping(source = "fpid", target = "fpid")
    ForgotPassword toEntity(ForgotPasswordDTO forgotPasswordDTO);

    @Mapping(source = "fpid", target = "fpid")
    ForgotPasswordDTO toDto(ForgotPassword forgotPassword);
}
