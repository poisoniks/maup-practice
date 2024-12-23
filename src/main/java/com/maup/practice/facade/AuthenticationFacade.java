package com.maup.practice.facade;

import com.maup.practice.dto.LoginRequest;
import com.maup.practice.dto.RegistrationRequest;

public interface AuthenticationFacade {
    void register(RegistrationRequest request);
    String login(LoginRequest request);
}
