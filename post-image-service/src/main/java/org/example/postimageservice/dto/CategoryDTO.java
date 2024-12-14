package org.example.postimageservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
