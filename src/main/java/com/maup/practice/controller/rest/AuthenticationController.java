package com.maup.practice.controller.rest;

import com.maup.practice.dto.LoginRequest;
import com.maup.practice.dto.RegistrationRequest;
import com.maup.practice.facade.AuthenticationFacade;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Autowired
    private AuthenticationFacade loginFacade;

    @PostMapping("/register")
    public ResponseEntity<Void> signup(@Valid @RequestBody RegistrationRequest requestDto) {
        loginFacade.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        String jwtToken = loginFacade.login(request);

        Cookie jwtCookie = new Cookie("JWT", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) (jwtExpiration/1000));
        response.addCookie(jwtCookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("JWT", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        return ResponseEntity.ok().build();
    }
}
