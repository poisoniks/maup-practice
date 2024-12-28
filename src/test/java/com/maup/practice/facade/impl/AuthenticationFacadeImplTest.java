package com.maup.practice.facade.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.dto.LoginRequest;
import com.maup.practice.dto.RegistrationRequest;
import com.maup.practice.exception.EmailExistsException;
import com.maup.practice.model.BasketModel;
import com.maup.practice.model.UserModel;
import com.maup.practice.service.BasketService;
import com.maup.practice.service.JWTService;
import com.maup.practice.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthenticationFacadeImplTest {

    @Mock
    private UserService userService;

    @Mock
    private JWTService jwtService;

    @Mock
    private BasketService basketService;

    @InjectMocks
    private AuthenticationFacadeImpl authenticationFacade;

    @Test
    void shouldRegisterUserWhenEmailNotExists() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("newuser@example.com");
        request.setPassword("password123");

        when(userService.existsByEmail("newuser@example.com")).thenReturn(false);

        authenticationFacade.register(request);

        verify(userService, times(1)).existsByEmail("newuser@example.com");
        verify(userService, times(1)).createUser(request);
    }

    @Test
    void shouldThrowEmailExistsExceptionWhenEmailExists() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("existing@example.com");
        request.setPassword("password123");

        when(userService.existsByEmail("existing@example.com")).thenReturn(true);

        EmailExistsException exception = assertThrows(EmailExistsException.class,
                () -> authenticationFacade.register(request));

        assertEquals("User with the email address 'existing@example.com' already exists.", exception.getMessage());
        verify(userService, times(1)).existsByEmail("existing@example.com");
        verify(userService, never()).createUser(any());
    }

    @Test
    void shouldLoginAndReturnTokenWhenCredentialsAreValid() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("validuser@example.com");
        loginRequest.setPassword("validPassword");

        UserModel user = new UserModel();
        user.setEmail("validuser@example.com");
        user.setPassword("hashedPassword");
        user.setAnonymous(false);

        when(userService.findByEmail("validuser@example.com")).thenReturn(user);
        when(userService.checkPassword(user, "validPassword")).thenReturn(true);
        when(jwtService.generateToken("validuser@example.com")).thenReturn("jwtToken");
        when(jwtService.extractUsername("jwtToken")).thenReturn("validuser@example.com");
        when(userService.getCurrentUser()).thenReturn(null); // No current user

        String token = authenticationFacade.login(loginRequest);

        assertEquals("jwtToken", token);
        verify(userService, times(2)).findByEmail("validuser@example.com");
        verify(userService, times(1)).checkPassword(user, "validPassword");
        verify(jwtService, times(1)).generateToken("validuser@example.com");
        verify(jwtService, times(1)).extractUsername("jwtToken");
        verify(userService, times(1)).getCurrentUser();
        verify(basketService, never()).mergeBaskets(any(), any());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCredentialsAreInvalid() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("invaliduser@example.com");
        loginRequest.setPassword("wrongPassword");

        when(userService.findByEmail("invaliduser@example.com")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationFacade.login(loginRequest));

        assertEquals("Invalid username or password", exception.getMessage());
        verify(userService, times(1)).findByEmail("invaliduser@example.com");
        verify(userService, never()).checkPassword(any(), any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void shouldMergeBasketsWhenCurrentUserIsAnonymous() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("validuser@example.com");
        loginRequest.setPassword("validPassword");

        UserModel loggedInUser = new UserModel();
        loggedInUser.setEmail("validuser@example.com");
        loggedInUser.setAnonymous(false);
        loggedInUser.setBasket(new BasketModel());

        UserModel anonymousUser = new UserModel();
        anonymousUser.setEmail("anonymous@example.com");
        anonymousUser.setAnonymous(true);
        anonymousUser.setBasket(new BasketModel());

        when(userService.findByEmail("validuser@example.com")).thenReturn(loggedInUser);
        when(userService.checkPassword(loggedInUser, "validPassword")).thenReturn(true);
        when(jwtService.generateToken("validuser@example.com")).thenReturn("jwtToken");
        when(jwtService.extractUsername("jwtToken")).thenReturn("validuser@example.com");
        when(userService.getCurrentUser()).thenReturn(anonymousUser);

        String token = authenticationFacade.login(loginRequest);

        assertEquals("jwtToken", token);
        verify(basketService, times(1)).mergeBaskets(loggedInUser, anonymousUser.getBasket());
    }

    @Test
    void shouldCreateAnonymousUserAndReturnTokenWhenAnonymousLoginIsCalled() {
        UserModel anonymousUser = new UserModel();
        anonymousUser.setEmail("anon@example.com");
        anonymousUser.setAnonymous(true);

        when(userService.createAnonymousUser()).thenReturn(anonymousUser);
        when(jwtService.generateToken("anon@example.com")).thenReturn("anonymousJwtToken");

        String token = authenticationFacade.anonymousLogin();

        assertEquals("anonymousJwtToken", token);
        verify(userService, times(1)).createAnonymousUser();
        verify(jwtService, times(1)).generateToken("anon@example.com");
    }

    @Test
    void shouldGenerateJWTCookieWhenTokenIsProvided() {
        String jwtToken = "sampleJwtToken";
        Cookie expectedCookie = new Cookie("JWT", jwtToken);
        expectedCookie.setHttpOnly(true);
        expectedCookie.setPath("/");

        when(jwtService.generateJWTCookie(jwtToken)).thenReturn(expectedCookie);

        Cookie cookie = authenticationFacade.generateJWTCookie(jwtToken);

        assertEquals(expectedCookie, cookie);
        verify(jwtService, times(1)).generateJWTCookie(jwtToken);
    }
}
