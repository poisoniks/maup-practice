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
    public UserProfileDTO convert(UserModel source) {
        if (source == null) {
            return null;
        }

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setEmail(source.getEmail());
        userProfileDTO.setFirstName(source.getFirstName());
        userProfileDTO.setLastName(source.getLastName());
        userProfileDTO.setPhone(source.getPhoneNumber());
        userProfileDTO.setAnonymous(source.isAnonymous());
        userProfileDTO.setManager(hasRole(source, ROLE_MANAGER));
        userProfileDTO.setAdmin(hasRole(source, ROLE_ADMIN));
        return userProfileDTO;
    }

    private boolean hasRole(UserModel userModel, String roleName) {
        return userModel.getRoles().stream().anyMatch(role -> role.getName().equals(roleName));
    }
}
