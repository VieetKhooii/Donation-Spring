package com.gabriel.donation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class DonationPostDTO {
    @JsonProperty("donation_post_id")
    private int donationPostId;

    private String title;

    @JsonProperty("start_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date startDate;

    @JsonProperty("end_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date endDate;

    @JsonProperty("goal_amount")
    private long goalAmount;
    @JsonProperty("current_amount")
    private long currentAmount;
    @JsonProperty("number_of_donation")
    private int numberOfDonation;

    private String story;

    @JsonProperty("receiver_id")
    private int receiverId;
    @JsonProperty("category_id")
    private int categoryId;
    @JsonProperty("is_deleted")
    private boolean isDeleted;
    @JsonProperty("images")
    private List<ImageOfDonationDTO> lstImages = new ArrayList<>();
}
