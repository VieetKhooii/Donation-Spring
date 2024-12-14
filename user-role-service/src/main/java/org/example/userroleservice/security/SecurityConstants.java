package org.example.userroleservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {
    public static final long JWT_EXPIRATION = 1000*60*10;
    @Value("${spring.security.jwt.secret-key}")
    private String JWT_SECRET; // Now an instance variable

    public String getJwtSecret() {
        return JWT_SECRET;
    }
}
