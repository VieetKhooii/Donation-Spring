package org.example.userroleservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerator {

    @Autowired
    private SecurityConstants securityConstants;

    public String generateToken(Authentication authentication){
        String phone = authentication.getName();
        return buildToken(phone);
    }

    public String generateToken(String email){
        return buildToken(email);
    }

    private String buildToken(String requiredString) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
        String token = Jwts.builder()
                .setSubject(requiredString)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, securityConstants.getJwtSecret())
                .compact();
        return token;
    }

    public long getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration().getTime();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(securityConstants.getJwtSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserNameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(securityConstants.getJwtSecret())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(securityConstants.getJwtSecret()).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect"+ e.getMessage());
        }
    }
}
