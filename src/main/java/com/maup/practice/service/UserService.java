package com.maup.practice.service;

import com.maup.practice.dto.RegistrationRequest;
import com.maup.practice.model.UserModel;

public interface UserService {
    void saveUser(UserModel user);
    void deleteUser(UserModel user);
    boolean existsByEmail(String email);
    boolean checkPassword(UserModel user, String password);
    UserModel findByEmail(String email);
    UserModel createUser(RegistrationRequest form);
    UserModel createAnonymousUser();
    UserModel getCurrentUser();
}
