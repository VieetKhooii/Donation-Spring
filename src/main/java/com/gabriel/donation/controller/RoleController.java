package com.gabriel.donation.controller;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.PaymentDTO;
import com.gabriel.donation.dto.RoleDTO;
import com.gabriel.donation.entity.Role;
import com.gabriel.donation.service.DonationPostService;
import com.gabriel.donation.service.RoleService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/role")
@Mapper
public class RoleController {
    /*
     * Các api cần làm:
     *   - Admin:
     *       + Lấy toàn bộ role [/admin/get] (có phân trang)
     *       + Sửa [/admin/edit]
     *       + Xóa [/admin/hide]
     */

    @Autowired
    RoleService roleService;

    @GetMapping("/admin/get")
    public String getAllRole(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<RoleDTO> list = roleService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<RoleDTO> roles = list.getContent();
        model.addAttribute("donationPost", roles);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/Role";

    }

    @GetMapping("/admin/hide/{id}")
    public String deleteCategory(@PathVariable("id") int id, Model model) {

        roleService.deleteRole(id);
        return "redirect:/admin/get";
    }

    @PostMapping("/admin/edit/{id}")
    public String showUpdateForm(
            @PathVariable("id") int id,
            Model model
    ) {
        RoleDTO roleDTO = roleService.getRoleById(id);
        model.addAttribute("role", roleDTO);
        return "admin/updateRole";
    }

    @PostMapping("/updateRole")
    public String updateRole(
            @ModelAttribute("role") RoleDTO roleDTO,
            Model model
    ) {
        roleService.updateRole(roleDTO, roleDTO.getRoleId());
        return "redirect:/admin/get";
    }
}
