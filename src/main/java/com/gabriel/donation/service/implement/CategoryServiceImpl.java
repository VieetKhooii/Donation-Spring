package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.CategoryDTO;
import com.gabriel.donation.entity.Category;
import com.gabriel.donation.repository.CategoryRepo;
import com.gabriel.donation.mapper.CategoryMapper;

import com.gabriel.donation.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepo cateRepo;

    @Override
    public Page<CategoryDTO> getAll(PageRequest pageRequest) {
        // Fetch categories with pagination
        Page<Category> categoriesPage = cateRepo.findAll(pageRequest);
        List<Category> categories = categoriesPage.getContent();

        // Map categories to DTOs
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(CategoryMapper.INSTANCE::toDto)
                .toList();

        // Return a PageImpl object with the mapped DTOs
        return new PageImpl<CategoryDTO>(
                categoryDTOS,
                categoriesPage.getPageable(),
                categoriesPage.getTotalElements()
        );
    }


    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO)
    {
        Category category=CategoryMapper.INSTANCE.toEntity(categoryDTO);
        Category savedCategory=cateRepo.save(category);
        return  CategoryMapper.INSTANCE.toDto(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, int id)
    {
        Category cate1=cateRepo.findById(id).get();
        cate1.setName(categoryDTO.getName());
//        cate1.setDeleted(categoryDTO.isDeleted());
        Category updatedCategory=cateRepo.save(cate1);
        return CategoryMapper.INSTANCE.toDto(updatedCategory);
    }

    @Override
    public void deleteCategory(int id)
    {
        if(cateRepo.existsById(id))
            cateRepo.deleteById(id);
    }

    @Override
    public CategoryDTO getCategoryById(int id)
    {
        return CategoryMapper.INSTANCE.toDto(cateRepo.findById(id).get());
    }

}
