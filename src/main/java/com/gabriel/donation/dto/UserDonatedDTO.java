package com.gabriel.donation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserDonatedDTO {
    @JsonProperty("user_donated_id")
    private int userDonatedId;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("donation_post_id")
    private int donationPostId;

    private long amount;

    private boolean anonymous = false;

    @JsonProperty("donate_date")
    private Date donateDate;

    @JsonProperty("is_deleted")
    private boolean isDeleted;
}
