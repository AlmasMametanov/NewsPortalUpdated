package com.newsPortal.NewsPortalUpdated.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;


@Component
public class JWTUtil {
    @Value("${jwt_secret}")
    private String secret;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return Jwts.builder()
                .setSubject("User details")
                .claim("email", email)
                .setIssuedAt(new Date())
                .setIssuer("NewsPortalUpdated")
                .setExpiration(expirationDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateTokenAndRetrieveClaim(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("email", String.class);
    }

}
