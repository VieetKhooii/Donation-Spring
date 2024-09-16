package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.CategoryDTO;
import com.gabriel.donation.entity.Category;
import com.gabriel.donation.repository.CategoryRepo;
import com.gabriel.donation.mapper.CategoryMapper;

import com.gabriel.donation.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepo cateRepo;

    @Override
    public List<CategoryDTO> getAll()
    {
        List<Category> categories=cateRepo.findAll();
        return  categories.stream()
                .map(CategoryMapper.INSTANCE::toDto)
                .toList();
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
        cate1.setDeleted(categoryDTO.isDeleted());
        Category updatedCategory=cateRepo.save(cate1);
        return CategoryMapper.INSTANCE.toDto(updatedCategory);
    }

    @Override
    public void deleteCategory(int id)
    {
        if(cateRepo.existsById(id))
            cateRepo.deleteById(id);
    }

}
