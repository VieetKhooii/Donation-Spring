package com.gabriel.donation.controller;

import com.gabriel.donation.dto.AuthResponseDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.security.JwtGenerator;
import com.gabriel.donation.service.UserService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtGenerator jwtGenerator;

    @PostMapping("login")
    public String login(
            @ModelAttribute UserDTO userDTO,
            HttpSession session,
            HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getPhone(),
                        userDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        UserDTO userDTO1 = userService.findByPhone(userDTO.getPhone());
        session.setAttribute("userId", userDTO1.getUserId());
        session.setAttribute("password", userDTO.getPassword());

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return "redirect:/home";
    }

    @PostMapping("register")
    public String register(@ModelAttribute UserDTO userDTO,
                           Model model) {
        String message = userService.register(userDTO);
        model.addAttribute("message", message);
        return "";
    }
}
