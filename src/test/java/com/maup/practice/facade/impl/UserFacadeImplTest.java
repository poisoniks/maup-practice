package com.maup.practice.facade.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.dto.UserProfileDTO;
import com.maup.practice.model.UserModel;
import com.maup.practice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;

@ExtendWith(MockitoExtension.class)
public class UserFacadeImplTest {

    @Mock
    private UserService userService;

    @Mock
    private Converter<UserModel, UserProfileDTO> userConverter;

    @InjectMocks
    private UserFacadeImpl userFacade;

    @Test
    void shouldGetUserProfileWhenUserIsAuthenticated() {
        UserModel user = new UserModel();
        user.setId(1L);
        user.setEmail("profile@example.com");

        UserProfileDTO dto = new UserProfileDTO();
        dto.setEmail("profile@example.com");

        when(userService.getCurrentUser()).thenReturn(user);
        when(userConverter.convert(user)).thenReturn(dto);

        UserProfileDTO result = userFacade.getUserProfile();

        verify(userService, times(1)).getCurrentUser();
        verify(userConverter, times(1)).convert(user);
        assertEquals(dto, result);
    }

    @Test
    void shouldGetUserProfileWhenUserIsAnonymous() {
        UserModel user = new UserModel();
        user.setId(2L);
        user.setEmail("anonymous@example.com");

        UserProfileDTO dto = new UserProfileDTO();
        dto.setEmail("anonymous@example.com");

        when(userService.getCurrentUser()).thenReturn(user);
        when(userConverter.convert(user)).thenReturn(dto);

        UserProfileDTO result = userFacade.getUserProfile();

        verify(userService, times(1)).getCurrentUser();
        verify(userConverter, times(1)).convert(user);
        assertEquals(dto, result);
    }

    @Test
    void shouldGetUserProfileWhenUserIsNull() {
        when(userService.getCurrentUser()).thenReturn(null);

        UserProfileDTO result = userFacade.getUserProfile();

        verify(userService, times(1)).getCurrentUser();
        verify(userConverter, times(1)).convert(null);
        assertNull(result);
    }
}
