package com.gabriel.donation.controller;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.RoleDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.service.DonationPostService;
import com.gabriel.donation.service.ImageOfDonationService;
import com.gabriel.donation.service.RoleService;
import com.gabriel.donation.service.UserService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/admin")
@Mapper
public class adminController {
    @Autowired
    UserService userService;
    @Autowired
    DonationPostService donationPostService;
    @Autowired
    ImageOfDonationService imageOfDonationService;
    @Autowired
    RoleService roleService;

    @GetMapping("user/get")
    @Cacheable("Admin")
    public String getAdmin(
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model
    ) {
        int limit = 7;
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<UserDTO> list = userService.getUsersForAdmin(pageRequest);
        int totalPages = list.getTotalPages();
        List<UserDTO> users = list.getContent();
        model.addAttribute("users", users);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/admin";
    }
    @GetMapping("/donation_post/get")
    @Cacheable("donationPostAdmin")
    public String getAllDonationPost(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<DonationPostDTO> list = getDonationPostDTOPage(pageRequest);
        int totalPages = list.getTotalPages();
        List<DonationPostDTO> donations = list.getContent();
        model.addAttribute("donationPost", donations);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/admin";
    }
    public Page<DonationPostDTO> getDonationPostDTOPage(PageRequest pageRequest) {
        return donationPostService.getAll(pageRequest);
    }

    @GetMapping("/role/get")
    public String getAllRole(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<RoleDTO> list = roleService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<RoleDTO> roles = list.getContent();
        model.addAttribute("roles", roles);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/admin";
    }
}
