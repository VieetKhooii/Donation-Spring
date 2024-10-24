package com.gabriel.donation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserDTO {
    @JsonProperty("user_id")
    private int userId;
    private String username;
    private String phone;
    private String password;
    private String email;
    private long balance;
    @JsonProperty("role_id")
    private int roleId;
    @JsonProperty("is_deleted")
    private boolean isDeleted;
}
