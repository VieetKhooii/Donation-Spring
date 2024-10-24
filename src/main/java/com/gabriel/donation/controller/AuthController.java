package com.gabriel.donation.controller;

import com.gabriel.donation.dto.AuthResponseDTO;
import com.gabriel.donation.dto.RoleDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.Role;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.mapper.RoleMapper;
import com.gabriel.donation.mapper.UserMapper;
import com.gabriel.donation.security.JwtGenerator;
import com.gabriel.donation.service.RoleService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        if (userService.exitsByPhone(userDTO.getPhone())) {
            return new ResponseEntity<>("Info is taken!", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setName(userDTO.getUsername());
        user.setPhone(userDTO.getPhone());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        RoleDTO roleDTO = roleService.findByName("USER");
        Role role = RoleMapper.INSTANCE.toEntity(roleDTO);
        user.setRole(role);

        UserDTO user1 = UserMapper.INSTANCE.toDto(user);
        userService.addUser(user1);
        return new ResponseEntity<>("User registered success", HttpStatus.OK);
    }
}
