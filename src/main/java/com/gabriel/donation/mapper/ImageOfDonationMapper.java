package com.gabriel.donation.mapper;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.ImageOfDonationDTO;
import com.gabriel.donation.entity.DonationPost;
import com.gabriel.donation.entity.ImageOfDonation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ImageOfDonationMapper {
    ImageOfDonationMapper INSTANCE = Mappers.getMapper(ImageOfDonationMapper.class);

    //               dto's id    entity's id
    @Mapping(source = "imageOfDonationId", target = "id")
    ImageOfDonation toEntity(ImageOfDonationDTO imageOfDonationDTO);

    @Mapping(source = "id", target = "imageOfDonationId")
    @Mapping(source = "donation_post.id", target = "donation_postId")
    // Fields from entity that are Object type (Foreign key) must have ".id" (ex: role.id)
    // so that DTO can understand and store it
    ImageOfDonationDTO toDto(ImageOfDonation imageOfDonation);
}
