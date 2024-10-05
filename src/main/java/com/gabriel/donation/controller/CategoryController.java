package com.gabriel.donation.controller;

import com.gabriel.donation.dto.CategoryDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.service.CategoryService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/category")
@Mapper
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/admin/get")
    @Cacheable("categoriesAdmin")
    public String getAllCategories(Model model) {
        List<CategoryDTO> categories = categoryService.getAll();
        model.addAttribute("categories", categories);

        return "admin/category";
    }

    @GetMapping("/admin/add")
    public String showAddCategoryForm(Model model)
    {
        model.addAttribute("category", new CategoryDTO());
        return "admin/addCategory";
    }

    @PostMapping("/saveCategory")
    public String saveUser(
            @ModelAttribute("category") CategoryDTO categoryDTO,
            Model model
    )
    {
        categoryService.addCategory(categoryDTO);
        return "redirect:/admin/get";
    }

    @GetMapping("/admin/hide/{id}")
    public String deleteCategory(@PathVariable("id") int id, Model model) {

        categoryService.deleteCategory(id);
        return "redirect:/admin/get";
    }

    @PostMapping("/admin/edit/{id}")
    public String showUpdateCategoryForm(
            @PathVariable("id") int id,
            Model model
    ) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        model.addAttribute("category", categoryDTO);
        return "admin/updateCategory";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(
            @ModelAttribute("category") CategoryDTO categoryDTO,
            Model model
    ) {
        categoryService.updateCategory(categoryDTO, categoryDTO.getCategoryId());
        return "redirect:/admin/get";
    }


    //User
    @GetMapping("/get")
    @Cacheable("categoriesUser")
    public String getAllCategoriesForUser(Model model) {
        List<CategoryDTO> categories = categoryService.getAll();
        model.addAttribute("categories", categories);

        return "user/category";
    }
}
