package com.maup.practice.facade.impl;

import com.maup.practice.dto.UserProfileDTO;
import com.maup.practice.facade.UserFacade;
import com.maup.practice.model.UserModel;
import com.maup.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class UserFacadeImpl implements UserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private Converter<UserModel, UserProfileDTO> userConverter;

    @Override
    public UserProfileDTO getUserProfile() {
        UserModel user = userService.getCurrentUser();
        return userConverter.convert(user);
    }
}
