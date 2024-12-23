package com.maup.practice.service;

import jakarta.servlet.http.Cookie;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String generateToken(String email);

    String extractUsername(String token);

    Boolean validateToken(String token, UserDetails userDetails);

    Cookie generateJWTCookie(String jwtToken);
}
