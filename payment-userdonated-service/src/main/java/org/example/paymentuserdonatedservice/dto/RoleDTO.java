package org.example.paymentuserdonatedservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RoleDTO {
    @JsonProperty("role_id")
    private int roleId;

    private String name;

    private String description;

    @JsonProperty("is_deleted")
    private boolean isDeleted;
}
