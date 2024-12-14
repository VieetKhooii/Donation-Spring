package org.example.postimageservice.mapper;

import com.gabriel.donation.dto.UserDonatedDTO;
import com.gabriel.donation.entity.UserDonated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDonatedMapper {
    UserDonatedMapper INSTANCE = Mappers.getMapper(UserDonatedMapper.class);

    //               dto's id    entity's id
    @Mapping(source = "userDonatedId", target = "id")
    @Mapping(source = "userId", target = "user.id")  // Mapping the userId to user entity's id
    @Mapping(source = "donationPostId", target = "donationPost.id")
    @Mapping(target = "user.name", ignore = true)
    UserDonated toEntity(UserDonatedDTO userDonatedDTO);

    @Mapping(source = "id", target = "userDonatedId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "donationPost.id", target = "donationPostId")
    @Mapping(target = "userName", expression = "java(userDonated.isAnonymous() ? \"Ẩn danh\" : userDonated.getUser().getName())")
    // Fields from entity that are Object type (Foreign key) must have ".id" (ex: role.id)
    // so that DTO can understand and store it
    UserDonatedDTO toDto(UserDonated userDonated);

    @Mapping(source = "username", target = "userName")
    UserDonatedDTO toDto(UserDonated.UserDonatedProjection userDonatedProjection);
}
