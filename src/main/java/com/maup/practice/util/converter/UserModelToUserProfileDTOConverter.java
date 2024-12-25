package com.maup.practice.util.converter;

import com.maup.practice.dto.UserProfileDTO;
import com.maup.practice.model.UserModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.maup.practice.util.Constants.ROLE_ADMIN;
import static com.maup.practice.util.Constants.ROLE_MANAGER;

@Component
public class UserModelToUserProfileDTOConverter implements Converter<UserModel, UserProfileDTO> {

    @Override
    public UserProfileDTO convert(UserModel userModel) {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setEmail(userModel.getEmail());
        userProfileDTO.setFirstName(userModel.getFirstName());
        userProfileDTO.setLastName(userModel.getLastName());
        userProfileDTO.setPhone(userModel.getPhoneNumber());
        userProfileDTO.setAnonymous(userModel.isAnonymous());
        userProfileDTO.setManager(hasRole(userModel, ROLE_MANAGER));
        userProfileDTO.setAdmin(hasRole(userModel, ROLE_ADMIN));
        return userProfileDTO;
    }

    private boolean hasRole(UserModel userModel, String roleName) {
        return userModel.getRoles().stream().anyMatch(role -> role.getName().equals(roleName));
    }
}
