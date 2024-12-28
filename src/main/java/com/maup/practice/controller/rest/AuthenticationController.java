package com.maup.practice.controller.rest;

import com.maup.practice.dto.LoginRequest;
import com.maup.practice.dto.RegistrationRequest;
import com.maup.practice.exception.EmailExistsException;
import com.maup.practice.facade.AuthenticationFacade;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.maup.practice.util.Constants.JWT_COOKIE;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final String JWT_EXPIRED_COOKIE = "JWT=; Path=/; Secure; HttpOnly; SameSite=None; Max-Age=0";
    private static final String SET_COOKIE_HEADER = "Set-Cookie";

    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public AuthenticationController(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping("/register")
    public ResponseEntity<List<ObjectError>> signup(@Valid @RequestBody RegistrationRequest requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }
        try {
            authenticationFacade.register(requestDto);
        } catch (EmailExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        String jwtToken;

        try {
            jwtToken = authenticationFacade.login(request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Cookie jwtCookie = authenticationFacade.generateJWTCookie(jwtToken);
        response.addCookie(jwtCookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie(JWT_COOKIE, null);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        jwtCookie.setSecure(true);
        jwtCookie.setHttpOnly(true);

        response.addCookie(jwtCookie);

        response.setHeader(SET_COOKIE_HEADER, JWT_EXPIRED_COOKIE);

        return ResponseEntity.ok().build();
    }

}
