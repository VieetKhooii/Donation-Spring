package org.example.userroleservice.mapper;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.ImageOfDonationDTO;
import com.gabriel.donation.entity.DonationPost;
import com.gabriel.donation.entity.ImageOfDonation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DonationPostMapper {
    DonationPostMapper INSTANCE = Mappers.getMapper(DonationPostMapper.class);

    //               dto's id    entity's id
    @Mapping(source = "donationPostId", target = "id")
    @Mapping(source = "receiverId", target = "receiver.id")
    DonationPost toEntity(DonationPostDTO donationPostDTO);

    @Mapping(source = "id", target = "donationPostId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "receiver.id", target = "receiverId")
    // Fields from entity that are Object type (Foreign key) must have ".id" (ex: role.id)
    // so that DTO can understand and store it
    DonationPostDTO toDto(DonationPost donationPost);
    ImageOfDonationDTO toDto(ImageOfDonation imageOfDonation);

}
