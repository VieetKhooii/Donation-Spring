package com.gabriel.donation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loginController {
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/register")
    public String register() {return "register";}
    @GetMapping("/forgotPassword")
    public String forgot() {return "forgotPassword";}
    @GetMapping("/enterOtp")
    public String enterOtp() {return "enterOtp";}
    @GetMapping("/resetPassword")
    public String resetPassword() {return "resetPassword";}
    @GetMapping("/api/admin/user")
    public String admin(){
        return "admin/user";
    }

}
