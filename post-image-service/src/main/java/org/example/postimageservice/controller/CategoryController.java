package org.example.postimageservice.controller;

import com.gabriel.donation.dto.CategoryDTO;
import com.gabriel.donation.dto.PaymentDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.service.CategoryService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/api/category")
@Mapper
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/admin/get")
    @Cacheable("categoriesAdmin")
    public String getAllCategories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            Model model
    )  {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<CategoryDTO> list = categoryService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<CategoryDTO> categories = list.getContent();
        model.addAttribute("categories", categories);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/Category/category";
    }

    @GetMapping("/admin/add")
    public String showAddCategoryForm(Model model)
    {
        model.addAttribute("category", new CategoryDTO());
        return "admin/Category/addCategory";
    }

    @PostMapping("/saveCategory")
    public String saveUser(
            @ModelAttribute("category") CategoryDTO categoryDTO,
            Model model
    )
    {
        categoryDTO.setDeleted(false);
        categoryService.addCategory(categoryDTO);
        return "redirect:/api/admin/user/get";
    }

    @GetMapping("/admin/hide/{id}")
    public String deleteCategory(@PathVariable("id") int id, Model model) {

        categoryService.deleteCategory(id);
        return "redirect:/api/admin/user/get";
    }

    @GetMapping("/admin/edit/{id}")
    public String showUpdateCategoryForm(
            @PathVariable("id") int id,
            Model model
    ) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        model.addAttribute("category", categoryDTO);
        return "admin/Category/updateCategory";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(
            @ModelAttribute("category") CategoryDTO categoryDTO,
            Model model
    ) {
        categoryService.updateCategory(categoryDTO, categoryDTO.getCategoryId());
        return "redirect:/api/admin/user/get";
    }


    //User
    @GetMapping("/get")
    @Cacheable("categoriesUser")
    public String getAllCategoriesForUser(Model model) {
//        List<CategoryDTO> categories = categoryService.getAll();
//        model.addAttribute("categories", categories);

        return "user/category";
    }
}
