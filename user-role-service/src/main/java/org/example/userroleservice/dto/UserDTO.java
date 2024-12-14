package org.example.userroleservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserDTO {
    @JsonProperty("user_id")
    private int userId;
    private String name;
    private String phone;
    private String password;
    private String email;
    private long balance;
    @JsonProperty("role_id")
    private int roleId;
    @JsonProperty("is_deleted")
    private boolean isDeleted;
}
