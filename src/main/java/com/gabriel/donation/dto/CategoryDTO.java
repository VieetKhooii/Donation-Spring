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
public class CategoryDTO {
    @JsonProperty("category_id")
    private int categoryId;

    private String name;

    @JsonProperty("is_deleted")
    private boolean isDeleted;
}
