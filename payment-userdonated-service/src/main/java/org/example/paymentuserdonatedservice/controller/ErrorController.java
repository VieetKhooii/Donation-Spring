package org.example.paymentuserdonatedservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/error/401")
    public String error401() {
        return "error/401page";
    }

    @GetMapping("/error/403")
    public String error403() {
        return "error/403page";
    }
}
