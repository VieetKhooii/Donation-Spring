package org.example.userroleservice.mapper;

import com.gabriel.donation.dto.ImageOfDonationDTO;
import com.gabriel.donation.entity.ImageOfDonation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ImageOfDonationMapper {
    ImageOfDonationMapper INSTANCE = Mappers.getMapper(ImageOfDonationMapper.class);

    //               dto's id    entity's id
    @Mapping(source = "imageOfDonationId", target = "id")
    @Mapping(source = "donationPostId", target = "donationPost.id")
    ImageOfDonation toEntity(ImageOfDonationDTO imageOfDonationDTO);

    @Mapping(source = "id", target = "imageOfDonationId")
    @Mapping(source = "donationPost.id", target = "donationPostId")
    // Fields from entity that are Object type (Foreign key) must have ".id" (ex: role.id)
    // so that DTO can understand and store it
    ImageOfDonationDTO toDto(ImageOfDonation imageOfDonation);
}
