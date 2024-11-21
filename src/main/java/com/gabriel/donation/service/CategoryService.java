package com.gabriel.donation.service;

import com.gabriel.donation.dto.CategoryDTO;
import com.gabriel.donation.dto.DonationPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CategoryService {
    Page<CategoryDTO> getAll(PageRequest pageRequest);

    CategoryDTO addCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, int id);

    void deleteCategory(int id);

    CategoryDTO getCategoryById(int id);
}
