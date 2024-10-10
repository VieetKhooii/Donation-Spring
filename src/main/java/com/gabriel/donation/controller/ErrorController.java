package com.gabriel.donation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {
    @GetMapping("/error/401")
    public String error401() {
        return "error/401page";
    }
}
