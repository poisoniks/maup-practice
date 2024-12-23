package com.maup.practice.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String generateToken(String email);

    String extractUsername(String token);

    Boolean validateToken(String token, UserDetails userDetails);
}
