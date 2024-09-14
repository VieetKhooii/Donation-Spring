package com.gabriel.donation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ImageOfDonationDTO {
    @JsonProperty("image_of_donation_id")
    private int imageOfDonationId;

    private String description;

    private String url;

    @JsonProperty("donation_post_id")
    private int donationPostId;

    @JsonProperty("is_deleted")
    private boolean isDeleted;
}
