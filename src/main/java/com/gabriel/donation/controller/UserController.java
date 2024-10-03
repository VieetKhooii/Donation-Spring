package com.gabriel.donation.controller;

import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.payload.ResponseData;
import com.gabriel.donation.service.UserService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/user")
@Mapper
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/admin/get")
    public ResponseEntity<?> getAllUsers(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<UserDTO> list = userService.getUsersForAdmin(pageRequest);
        int totalPages = list.getTotalPages();
        List<UserDTO> users = list.getContent();
//        model.addAttribute("users", users);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("currentPage", page);
//        return "admin/user";
        return ResponseEntity.ok(users);

    }



}
