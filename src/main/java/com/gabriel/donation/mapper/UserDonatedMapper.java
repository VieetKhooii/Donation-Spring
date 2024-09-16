package com.gabriel.donation.mapper;

import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.dto.UserDonatedDTO;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.entity.UserDonated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDonatedMapper {
    UserDonatedMapper INSTANCE = Mappers.getMapper(UserDonatedMapper.class);

    //               dto's id    entity's id
    @Mapping(source = "userDonatedId", target = "id")
    UserDonated toEntity(UserDonatedDTO userDonatedDTO);

    @Mapping(source = "id", target = "userDonatedId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "donation_post.id", target = "donationPostId")
    // Fields from entity that are Object type (Foreign key) must have ".id" (ex: role.id)
    // so that DTO can understand and store it
    UserDonatedDTO toDto(UserDonated userDonated);
}
