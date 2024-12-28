package com.maup.practice.facade.impl;

import com.maup.practice.dto.LoginRequest;
import com.maup.practice.dto.RegistrationRequest;
import com.maup.practice.exception.EmailExistsException;
import com.maup.practice.facade.AuthenticationFacade;
import com.maup.practice.model.UserModel;
import com.maup.practice.service.BasketService;
import com.maup.practice.service.JWTService;
import com.maup.practice.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final UserService userService;
    private final JWTService jwtService;
    private final BasketService basketService;

    @Autowired
    public AuthenticationFacadeImpl(UserService userService, JWTService jwtService, BasketService basketService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.basketService = basketService;
    }

    @Override
    @Transactional
    public void register(RegistrationRequest request) {
        String email = request.getEmail();
        if (userService.existsByEmail(email)) {
            throw new EmailExistsException(String.format("User with the email address '%s' already exists.", email));
        }

        userService.createUser(request);
    }

    @Override
    @Transactional
    public String login(LoginRequest form) {
        UserModel user = userService.findByEmail(form.getUsername());
        if (user == null || !userService.checkPassword(user, form.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtService.generateToken(form.getUsername());
        String email = jwtService.extractUsername(token);
        UserModel loggedInUser = userService.findByEmail(email);
        UserModel currentUser = userService.getCurrentUser();
        if (currentUser != null && currentUser.isAnonymous()) {
            basketService.mergeBaskets(loggedInUser, currentUser.getBasket());
        }
        return token;
    }

    @Override
    @Transactional
    public String anonymousLogin() {
        UserModel anonymousUser = userService.createAnonymousUser();
        return jwtService.generateToken(anonymousUser.getEmail());
    }

    @Override
    public Cookie generateJWTCookie(String jwtToken) {
        return jwtService.generateJWTCookie(jwtToken);
    }
}
