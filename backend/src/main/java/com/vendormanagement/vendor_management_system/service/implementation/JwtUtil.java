package com.vendormanagement.vendor_management_system.service.implementation;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

//import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    // 🔐 Your key must be at least 256 bits (32 bytes) for HS256
    //private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // auto-generated
    private final SecretKey key = Keys.hmacShaKeyFor("secretkey1234567890secretkey1234567890".getBytes());

    // private final Key key = Keys.hmacShaKeyFor("secretkey1234567890secretkey1234567890".getBytes());

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Optionally log the error
            return false;
        }
    }
}
