package com.gabriel.donation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gabriel.donation.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ForgotPasswordDTO {
    @JsonProperty("fp_id")
    private Integer fpid;

    private Integer otp;

    private Date expirationTime;
}
