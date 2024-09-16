package com.gabriel.donation.mapper;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.DonationPost;
import com.gabriel.donation.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DonationPostMapper {
    DonationPostMapper INSTANCE = Mappers.getMapper(DonationPostMapper.class);

    //               dto's id    entity's id
    @Mapping(source = "donationPostId", target = "id")
    DonationPost toEntity(DonationPostDTO donationPostDTO);

    @Mapping(source = "id", target = "donationPostId")
    @Mapping(source = "sponsor.id", target = "sponsorId")
    @Mapping(source = "category.id", target = "categoryId")
    // Fields from entity that are Object type (Foreign key) must have ".id" (ex: role.id)
    // so that DTO can understand and store it
    DonationPostDTO toDto(DonationPost donationPost);
}
