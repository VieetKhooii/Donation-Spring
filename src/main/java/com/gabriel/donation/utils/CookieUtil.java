package com.gabriel.donation.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.donation.dto.UserDTO;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Base64;

@Component
public class CookieUtil {

    public String getCookieValue(Cookie[] cookies, String cookieName) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public UserDTO decodeUserDTOInCookie(String encodedCookieValue) throws JsonProcessingException {
        String userDTOJson = new String(Base64.getDecoder().decode(encodedCookieValue));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(userDTOJson, UserDTO.class);
    }
}
