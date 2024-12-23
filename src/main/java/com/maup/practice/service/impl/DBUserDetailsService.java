package com.maup.practice.service.impl;

import com.maup.practice.model.RoleModel;
import com.maup.practice.model.UserModel;
import com.maup.practice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class DBUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByEmail(username);
        if (userModel == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return User.withUsername(userModel.getEmail())
                .password(userModel.getPassword() == null ? "" : userModel.getPassword())
                .disabled(!userModel.isEnabled())
                .authorities(userModel.getRoles().stream()
                        .map(RoleModel::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()))
                .build();
    }
}