package com.gabriel.donation.mapper;

import com.gabriel.donation.dto.RoleDTO;
import com.gabriel.donation.dto.SponsorDTO;
import com.gabriel.donation.entity.Role;
import com.gabriel.donation.entity.Sponsor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SponsorMapper {
    SponsorMapper INSTANCE = Mappers.getMapper(SponsorMapper.class);

    //               dto's id    entity's id
    @Mapping(source = "sponsorId", target = "id")
    Sponsor toEntity(SponsorDTO sponsorDTO);

    @Mapping(source = "id", target = "sponsorId")

    SponsorDTO toDto(Sponsor sponsor);
}
