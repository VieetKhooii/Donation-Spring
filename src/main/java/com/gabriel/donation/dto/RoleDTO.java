package com.gabriel.donation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
