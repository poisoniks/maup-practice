package com.maup.practice.service.impl;

import com.maup.practice.model.UserModel;
import com.maup.practice.repository.UserRepository;
import com.maup.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUser(UserModel user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UserModel user) {
        userRepository.delete(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserModel findByEmail(String username) {
        return userRepository.findByEmail(username);
    }
}
