package org.example.postimageservice.service;

import com.gabriel.donation.dto.CategoryDTO;
import com.gabriel.donation.dto.DonationPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CategoryService {
    Page<CategoryDTO> getAll(PageRequest pageRequest);

    void addCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, int id);

    void deleteCategory(int id);

    CategoryDTO getCategoryById(int id);
}