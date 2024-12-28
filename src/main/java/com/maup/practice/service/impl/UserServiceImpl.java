package com.maup.practice.service.impl;

import com.maup.practice.dto.RegistrationRequest;
import com.maup.practice.model.UserModel;
import com.maup.practice.repository.RoleRepository;
import com.maup.practice.repository.UserRepository;
import com.maup.practice.service.UserService;
import com.maup.practice.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserModel user) {
        userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean checkPassword(UserModel user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public UserModel findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public UserModel createUser(RegistrationRequest form) {
        UserModel user = new UserModel();
        user.setRoles(Collections.singleton(roleRepository.findByName(Constants.ROLE_USER)));
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setEnabled(true);
        user.setFirstName(form.getFirstname());
        user.setLastName(form.getLastname());

        return userRepository.save(user);
    }

    @Override
    public UserModel createAnonymousUser() {
        String uniqueUsername = "anon-" + UUID.randomUUID() + "@user.com";

        UserModel anonymousUser = new UserModel();
        anonymousUser.setEmail(uniqueUsername);
        anonymousUser.setAnonymous(true);
        anonymousUser.setEnabled(true);
        anonymousUser.setRoles(Collections.singleton(roleRepository.findByName(Constants.ROLE_ANONYMOUS)));

        return userRepository.save(anonymousUser);
    }

    @Override
    public UserModel getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username);
    }
}
