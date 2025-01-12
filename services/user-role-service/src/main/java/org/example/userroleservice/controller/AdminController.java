package org.example.userroleservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.userroleservice.dto.RoleDTO;
import org.example.userroleservice.service.RoleService;
import org.example.userroleservice.service.UserService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/admin")
@Mapper
public class AdminController {
    @Autowired
    UserService userService;
//    @Autowired
//    DonationPostService donationPostService;
//    @Autowired
//    ImageOfDonationService imageOfDonationService;
    @Autowired
    RoleService roleService;

    @GetMapping("user/get")
    public String getAdmin(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "7") int limit,
            Model model
    ) {

        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
//        Page<UserDTO> list = userService.getUsersForAdmin(pageRequest);
//        int totalPages = list.getTotalPages();
//        List<UserDTO> users = list.getContent();
//        model.addAttribute("users", users);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("currentPage", page);
        return "admin/admin";
    }
//    @GetMapping("/donation_post/get")
//    @Cacheable("donationPostAdmin")
//    public String getAllDonationPost(
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "limit", defaultValue = "5") int limit,
//            Model model
//    ) {
//        PageRequest pageRequest = PageRequest.of(
//                page, limit
//        );
//        Page<DonationPostDTO> list = getDonationPostDTOPage(pageRequest);
//        int totalPages = list.getTotalPages();
//        List<DonationPostDTO> donations = list.getContent();
//        model.addAttribute("donationPost", donations);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("currentPage", page);
//        return "admin/admin";
//    }
//    public Page<DonationPostDTO> getDonationPostDTOPage(PageRequest pageRequest) {
//        return donationPostService.getAll(pageRequest);
//    }

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

    @GetMapping("/signout")
    public ResponseEntity<?> signOut(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        boolean isSignOut = userService.signOut(request, response);
        String ajaxHeader= request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(ajaxHeader))
        {
            return ResponseEntity.ok(Map.of(
                    "isSignOut",isSignOut,
                    "message","You have been signout successfully."));

        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location","/login")
                .build();
    }

}
