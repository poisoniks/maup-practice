package com.maup.practice.facade.impl;

import com.maup.practice.dto.UserRegistrationForm;
import com.maup.practice.facade.LoginFacade;
import com.maup.practice.model.UserModel;
import com.maup.practice.repository.RoleRepository;
import com.maup.practice.service.UserService;
import com.maup.practice.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class LoginFacadeImpl implements LoginFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserRegistrationForm form) {
        if (userService.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        UserModel user = new UserModel();
        user.setFirstName(form.getFirstname());
        user.setLastName(form.getLastname());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setEnabled(true);
        user.setRoles(Collections.singleton(roleRepository.findByName(Constants.ROLE_USER)));

        userService.saveUser(user);
    }
}
