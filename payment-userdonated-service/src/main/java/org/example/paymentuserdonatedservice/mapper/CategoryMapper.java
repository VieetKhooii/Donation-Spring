package org.example.paymentuserdonatedservice.mapper;

import com.gabriel.donation.dto.CategoryDTO;
import com.gabriel.donation.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    //               dto's id    entity's id
    @Mapping(source = "categoryId", target = "id")
    Category toEntity(CategoryDTO categoryDTO);

    @Mapping(source = "id", target = "categoryId")

    CategoryDTO toDto(Category category);
}