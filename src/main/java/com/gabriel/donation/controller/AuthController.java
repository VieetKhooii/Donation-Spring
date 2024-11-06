package com.gabriel.donation.controller;

import com.gabriel.donation.dto.AuthResponseDTO;
import com.gabriel.donation.dto.RoleDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.Role;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.mapper.RoleMapper;
import com.gabriel.donation.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.donation.payload.CookieName;
import com.gabriel.donation.security.JwtGenerator;
import com.gabriel.donation.service.RoleService;
import com.gabriel.donation.service.UserService;
import com.gabriel.donation.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Base64;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTOInput, HttpSession session, HttpServletResponse response) {
        if (userDTOInput.getPhone() == null || userDTOInput.getPhone().isEmpty() ||
                userDTOInput.getPassword() == null || userDTOInput.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thông tin đăng nhập không được để trống!");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTOInput.getPhone(), userDTOInput.getPassword()));

            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtGenerator.generateToken(authentication);
                UserDTO userDTO = userService.findByPhone(userDTOInput.getPhone());
                ObjectMapper objectMapper = new ObjectMapper();
                String userDTOJson = objectMapper.writeValueAsString(userDTO);
                String encodedUserDTOJson = Base64.getEncoder().encodeToString(userDTOJson.getBytes());

                session.setAttribute("userId", userDTO.getUserId());
                session.setAttribute("username", userDTO.getName());

                ResponseCookie cookie = ResponseCookie.from(String.valueOf(CookieName.jwt), token)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(600)
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

                System.out.println(userDTOJson);
                ResponseCookie userInfoCookie = ResponseCookie.from(String.valueOf(CookieName.userInfo), encodedUserDTOJson)
                        .httpOnly(true) // Có thể chỉnh sửa tùy nhu cầu
                        .secure(true)
                        .path("/")
                        .maxAge(600)
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE, userInfoCookie.toString());
                if (roleService.findRoleNameById(userDTO.getRoleId()).equalsIgnoreCase("USER")){
                    return ResponseEntity.ok("user");
                }
                else {
                    return ResponseEntity.ok("admin");
                }
            }
        } catch (AuthenticationException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Đã có lỗi xảy ra");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền truy cập");
    }

    @PostMapping("register")
    public String register(
            @RequestBody UserDTO userDTO,
            Model model) {
        if (userService.findByPhone(userDTO.getPhone()) != null) {
            model.addAttribute("message","Số điện thoại đã tồn tại");
            return "";
        }
        String message = userService.register(userDTO);
        return message;
    }


    @GetMapping("check-cookie")
    public ResponseEntity<String> checkCookie(
            HttpServletRequest request
    ) {
        try {
            System.out.println("Cookie Checking");
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                String token = cookieUtil.getCookieValue(cookies, String.valueOf(CookieName.jwt));
                if (token != null) {
                    boolean check = jwtGenerator.validateToken(token);
                    if (check) {
                        String userInfoJson = cookieUtil.getCookieValue(cookies, String.valueOf(CookieName.userInfo));
                        UserDTO userDTO = cookieUtil.decodeUserDTOInCookie(userInfoJson);
                        System.out.println("Found username "+userDTO.getName());
                        return new ResponseEntity<>(userDTO.getName(), HttpStatus.OK);
                    }
                    System.out.println("Validation jwt failed");
                }
                System.out.println("jwt not found");
            }
            System.out.println("Check cookie fail");
            return new ResponseEntity<>("", HttpStatus.EXPECTATION_FAILED);
        }
        catch (Exception e){

            return new ResponseEntity<>("", HttpStatus.BAD_GATEWAY);
        }
    }
}