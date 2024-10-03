package com.gabriel.donation.controller;

import com.gabriel.donation.dto.AuthResponseDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.Role;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.repository.RoleRepo;
import com.gabriel.donation.repository.UserRepo;
import com.gabriel.donation.security.JwtGenerator;
import com.gabriel.donation.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepo userRepo;
    private RoleRepo roleRepo;
    @Autowired
    UserService userService;
    private PasswordEncoder passwordEncoder;
    private JwtGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepo userRepo,
                          RoleRepo roleRepo, PasswordEncoder passwordEncoder,
                          JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody UserDTO userDTO, HttpSession session){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getPhone(),
                        userDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        UserDTO userDTO1 = userService.findByPhoneAndPassword(userDTO.getPhone());
        session.setAttribute("userId", userDTO1.getUserId());
        session.setAttribute("password", userDTO.getPassword());
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        if (userRepo.existsByPhone(userDTO.getPhone())) {
            return new ResponseEntity<>("Info is taken!", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setName(userDTO.getUsername());
        user.setPhone(userDTO.getPhone());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Role role = roleRepo.findByName("USER").get();
        user.setRole(role);

        userRepo.save(user);

        return new ResponseEntity<>("User registered success", HttpStatus.OK);
    }
}
