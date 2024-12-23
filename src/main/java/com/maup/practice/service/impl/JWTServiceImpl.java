package com.maup.practice.service.impl;

import com.maup.practice.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;

@Service
public class JWTServiceImpl implements JWTService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expirationTime;

    private Key secretKey;

    @PostConstruct
    public void initializeSecretKey() {
        secretKey = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    @Override
    public String generateToken(String email) {
        var now = Instant.now();
        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationTime, ChronoUnit.MILLIS)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private Claims getTokenBody(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException | ExpiredJwtException e) {
            throw new AccessDeniedException("Access denied: " + e.getMessage());
        }
    }

    private boolean isTokenExpired(String token) {
        Claims claims = getTokenBody(token);
        return claims.getExpiration().before(new Date());
    }
}
