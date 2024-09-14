package com.gabriel.donation.mapper;

import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //               dto's id    entity's id
    @Mapping(source = "userId", target = "id")
    User toEntity(UserDTO userDTO);

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "role.id", target = "roleId")
    // Fields from entity that are Object type (Foreign key) must have ".id" (ex: role.id)
    // so that DTO can understand and store it
    UserDTO toDto(User user);
}
