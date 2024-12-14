package org.example.postimageservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class homeController {
    @GetMapping("/")
    public String home()
    {
        return "redirect:/api/donation_post/get";
    }

    @GetMapping("/test")
    public void test(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/auth/check-cookie";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());

    }
}
