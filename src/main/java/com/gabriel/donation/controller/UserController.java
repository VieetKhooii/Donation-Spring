package com.gabriel.donation.controller;

import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.service.UserService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/user")
@Mapper
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/admin/get")
    @Cacheable("usersAdmin")
    public String getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model
    ) {
        int limit = 5;
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<UserDTO> list = userService.getUsersForAdmin(pageRequest);
        int totalPages = list.getTotalPages();
        List<UserDTO> users = list.getContent();
        model.addAttribute("users", users);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/user";
    }

    @PostMapping("/admin/add")
    public String addUser(
            @RequestBody UserDTO userDTO,
            Model model
    ) {
        try {
            userService.addUser(userDTO);
            model.addAttribute("message", "User added successfully");
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("message", "User already exists or data integrity issue");
        } catch (Exception e) {
            model.addAttribute("message", "An error occurred while adding the user");
        }
        return "admin/user";
    }

    @PostMapping("/admin/edit")
    public String editUser(
            @RequestBody UserDTO userDTO,
            @PathVariable int id,
            Model model
    ){
        try {
            userService.updateUser(userDTO, id);
            model.addAttribute("message", "User updated successfully");
        } catch (Exception e) {
            model.addAttribute("message", "An error occurred while adding the user");
        }
        return "admin/user";
    }

    @PostMapping("/admin/hide")
    public String hideUser(
            @PathVariable int id,
            Model model
    ){
        try {
            userService.deleteUser(id);
            model.addAttribute("message", "User hidden successfully");
        } catch (Exception e) {
            model.addAttribute("message", "An error occurred while adding the user");
        }
        return "admin/user";
    }

    // Test only, not use for Production
    @GetMapping("/getcache")
    public ResponseEntity<?> getCache(){
        return ResponseEntity.ok(userService.checkCache());
    }
}
