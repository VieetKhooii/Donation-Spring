package org.example.userroleservice.mapper;

import org.example.userroleservice.dto.ForgotPasswordDTO;
import org.example.userroleservice.entity.ForgotPassword;
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
