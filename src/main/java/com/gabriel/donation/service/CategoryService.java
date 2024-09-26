package com.gabriel.donation.service;

import com.gabriel.donation.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAll();

    CategoryDTO addCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, int id);

    void deleteCategory(int id);

    CategoryDTO getCategoryById(int id);
}
