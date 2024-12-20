package com.maup.practice.service;

import com.maup.practice.model.UserModel;

public interface UserService {
    void saveUser(UserModel user);
    void deleteUser(UserModel user);
    boolean existsByEmail(String email);
    UserModel findByUsername(String username);
}
