package org.example.userroleservice.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.userroleservice.dto.UserDTO;
import org.example.userroleservice.payload.CookieName;
import org.example.userroleservice.security.JwtGenerator;
import org.example.userroleservice.service.RoleService;
import org.example.userroleservice.service.UserService;
import org.example.userroleservice.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

//                session.setAttribute("userId", userDTO.getUserId());
                session.setAttribute("username", userDTO.getName());

                cookieUtil.createNewCookie(response, token, CookieName.jwt);
                cookieUtil.createNewCookie(response, userDTO);

                if (roleService.findRoleNameById(userDTO.getRoleId()).equalsIgnoreCase("ROLE_USER")){
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
    public ResponseEntity<?> register(
            @RequestBody UserDTO userDTO,
            Model model) {
        if (userService.findByPhone(userDTO.getPhone()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số điện thoại đã tồn tại");
        }
        String message = userService.register(userDTO);
        return ResponseEntity.ok("success");
    }


    @GetMapping("check-cookie")
    public ResponseEntity<String> checkCookie(
            HttpServletRequest request
    ) {
        try {
//            System.out.println("Cookie Checking");
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
//                    System.out.println("Validation jwt failed");
                }
//                System.out.println("jwt not found");
            }
//            System.out.println("Check cookie fail");
            return new ResponseEntity<>("", HttpStatus.EXPECTATION_FAILED);
        }
        catch (Exception e){

            return new ResponseEntity<>("", HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/signout")
    public String signOut(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        userService.signOut(request, response);
        return "redirect:/";
    }
}