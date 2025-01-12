package org.example.paymentuserdonatedservice.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.paymentuserdonatedservice.payload.CookieName;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CookieUtil {

    public String getCookieValue(Cookie[] cookies, String cookieName) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

//    public UserDTO decodeUserDTOInCookie(String encodedCookieValue) throws JsonProcessingException {
//        String userDTOJson = new String(Base64.getDecoder().decode(encodedCookieValue));
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(userDTOJson, UserDTO.class);
//    }

    public void setCookieToExpire(Cookie[] cookies, String cookieName, HttpServletResponse response) {
        Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .ifPresent(cookie -> {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                });
    }

//    public void createNewCookie(HttpServletResponse response, UserDTO userDTOInput) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userDTOJson = objectMapper.writeValueAsString(userDTOInput);
//        String encodedUserDTOJson = Base64.getEncoder().encodeToString(userDTOJson.getBytes());
//
//        createNewCookie(response, encodedUserDTOJson, CookieName.userInfo);
//    }

    public void createNewCookie(
            HttpServletResponse response,
            String value,
            CookieName cookieName) {

        ResponseCookie cookie = ResponseCookie.from(String.valueOf(cookieName), value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(600)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
