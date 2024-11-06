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

    @GetMapping("/api/admin/user")
    public String admin(){
        return "admin/user";
    }

}
