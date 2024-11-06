package com.gabriel.donation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.donation.dto.PaymentDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.payload.CookieName;
import com.gabriel.donation.service.PaymentService;
import com.gabriel.donation.service.UserService;
import com.gabriel.donation.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/user")
@Mapper
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    PaymentService paymentService;

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

    @GetMapping("/user_info")
    public String user_info(HttpSession session,
                            Model model,
                            HttpServletRequest request) throws JsonProcessingException {
//        int userId= (int) session.getAttribute("userId");
        Cookie[] cookie = request.getCookies();
        CookieUtil cookieUtil = new CookieUtil();
        String info = cookieUtil.getCookieValue(cookie, String.valueOf(CookieName.userInfo));
        UserDTO userDTO = cookieUtil.decodeUserDTOInCookie(info);
//        UserDTO userLogin=userService.findById(userId);
        model.addAttribute("userLogin", userDTO);
        return "user_information/user_info";
    }

    @GetMapping("/to_deposit")
    public String to_deposit(HttpSession session,
                            Model model){
        return "user_information/deposit";
    }

    @PostMapping("/deposit")
    public String deposit(
            @RequestParam("deposit_amount") long depositAmount,
            HttpSession session
    )
    {
        int userId = (int) session.getAttribute("userId");
        PaymentDTO paymentDTO=new PaymentDTO();
        paymentDTO.setAmount(depositAmount);
        paymentDTO.setUserId(userId);
        paymentDTO.setDeleted(false);
        paymentDTO.setTransactionDate(Date.valueOf(LocalDate.now()));
        paymentService.addPayment(paymentDTO);

        UserDTO userDTO = userService.findById(userId);
        userDTO.setBalance(userDTO.getBalance()+paymentDTO.getAmount());
        userService.updateUser(userDTO, userId);
        return  "redirect:/api/user/user_info";
    }

    @GetMapping("/to_changeInfo")
    public String to_changeInfo(HttpServletRequest request,
                             Model model) throws JsonProcessingException {
        Cookie[] cookie = request.getCookies();
        CookieUtil cookieUtil = new CookieUtil();
        String info = cookieUtil.getCookieValue(cookie, String.valueOf(CookieName.userInfo));
        UserDTO userDTO = cookieUtil.decodeUserDTOInCookie(info);
        model.addAttribute("userLogin", userDTO);
        return "user_information/change_info";
    }

    @PostMapping("changeInfo")
    public  ResponseEntity<?> changeInfo(@RequestBody UserDTO userDTOInput,
            HttpServletRequest request) throws JsonProcessingException {
        System.out.println(userDTOInput.getName());
        if (userDTOInput.getPhone() == null || userDTOInput.getPhone().isEmpty() ||
                userDTOInput.getName() == null || userDTOInput.getName().isEmpty()||
                userDTOInput.getEmail() == null || userDTOInput.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thông tin đăng nhập không được để trống!");
        }

        // Kiểm tra định dạng số điện thoại Việt Nam
        if (!userDTOInput.getPhone().matches("^0[0-9]{9}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số điện thoại không đúng định dạng Việt Nam!");
        }

        // Kiểm tra định dạng email
        if (!userDTOInput.getEmail().matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email không đúng định dạng!");
        }

        // Kiểm tra tên người dùng ít nhất 3 ký tự
        if (userDTOInput.getName().length() < 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tên người dùng phải có ít nhất 3 ký tự!");
        }
//        int userId= (int) session.getAttribute("userId");
        Cookie[] cookie = request.getCookies();
        CookieUtil cookieUtil = new CookieUtil();
        String info = cookieUtil.getCookieValue(cookie, String.valueOf(CookieName.userInfo));
        UserDTO userDTO = cookieUtil.decodeUserDTOInCookie(info);
        UserDTO existingUser = userService.findByPhone(userDTOInput.getPhone());
        if (existingUser != null && existingUser.getUserId() != userDTO.getUserId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số điện thoại đã được dùng");
        }
        userDTOInput.setBalance(existingUser.getBalance());
        userDTOInput.setRoleId(existingUser.getRoleId());
        userDTOInput.setDeleted(existingUser.isDeleted());
        userService.updateUser(userDTOInput, userDTO.getUserId());
//        session.setAttribute("username", userDTOInput.getName());
        return ResponseEntity.ok("update_success");
    }

//    @GetMapping("/to_changePassword")
//    public String to_changePassword(HttpServletRequest request,
//                                Model model) throws JsonProcessingException {
//        Cookie[] cookie = request.getCookies();
//        CookieUtil cookieUtil = new CookieUtil();
//        String info = cookieUtil.getCookieValue(cookie, String.valueOf(CookieName.userInfo));
//        UserDTO userDTO = cookieUtil.decodeUserDTOInCookie(info);
//        System.out.println(userDTO.getPassword());
//        model.addAttribute("userLogin", userDTO);
//        return "user_information/change_password";
//    }

}
