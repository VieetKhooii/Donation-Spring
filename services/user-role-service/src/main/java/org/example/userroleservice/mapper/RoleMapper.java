package org.example.userroleservice.mapper;

import org.example.userroleservice.dto.RoleDTO;
import org.example.userroleservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    //               dto's id    entity's id
    @Mapping(source = "roleId", target = "id")
    Role toEntity(RoleDTO roleDTO);

    @Mapping(source = "id", target = "roleId")

    RoleDTO toDto(Role role);
}
