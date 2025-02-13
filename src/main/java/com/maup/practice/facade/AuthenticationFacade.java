package com.maup.practice.facade;

import com.maup.practice.dto.LoginRequest;
import com.maup.practice.dto.RegistrationRequest;
import jakarta.servlet.http.Cookie;

public interface AuthenticationFacade {
    void register(RegistrationRequest request);
    String login(LoginRequest request);
    String anonymousLogin();
    Cookie generateJWTCookie(String jwtToken);
}
