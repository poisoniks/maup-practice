package com.maup.practice.facade.impl;

import com.maup.practice.dto.LoginRequest;
import com.maup.practice.dto.RegistrationRequest;
import com.maup.practice.exception.EmailExistsException;
import com.maup.practice.facade.AuthenticationFacade;
import com.maup.practice.model.UserModel;
import com.maup.practice.repository.RoleRepository;
import com.maup.practice.service.JWTService;
import com.maup.practice.service.UserService;
import com.maup.practice.util.Constants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Override
    @Transactional
    public void register(RegistrationRequest request) {
        String email = request.getEmail();
        if (userService.existsByEmail(email)) {
            throw new EmailExistsException(String.format("User with the email address '%s' already exists.", email));
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        UserModel user = new UserModel();
        user.setRoles(Collections.singleton(roleRepository.findByName(Constants.ROLE_USER)));
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setEnabled(true);
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());

        userService.saveUser(user);
    }

    @Override
    @Transactional
    public String login(LoginRequest form) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));
        return jwtService.generateToken(form.getUsername());
    }
}
