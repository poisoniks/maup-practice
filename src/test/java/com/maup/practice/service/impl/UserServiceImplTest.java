package com.maup.practice.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.dto.RegistrationRequest;
import com.maup.practice.model.RoleModel;
import com.maup.practice.model.UserModel;
import com.maup.practice.repository.RoleRepository;
import com.maup.practice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldSaveUserWhenUserModelIsValid() {
        UserModel user = new UserModel();
        user.setId(1L);
        user.setEmail("test@example.com");

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        String email = "existing@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean exists = userService.existsByEmail(email);

        verify(userRepository, times(1)).existsByEmail(email);
        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        String email = "nonexistent@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        boolean exists = userService.existsByEmail(email);

        verify(userRepository, times(1)).existsByEmail(email);
        assertFalse(exists);
    }

    @Test
    void shouldReturnTrueWhenPasswordMatches() {
        UserModel user = new UserModel();
        user.setPassword("hashedPassword");

        String rawPassword = "password123";

        when(passwordEncoder.matches(rawPassword, "hashedPassword")).thenReturn(true);

        boolean matches = userService.checkPassword(user, rawPassword);

        verify(passwordEncoder, times(1)).matches(rawPassword, "hashedPassword");
        assertTrue(matches);
    }

    @Test
    void shouldReturnFalseWhenPasswordDoesNotMatch() {
        UserModel user = new UserModel();
        user.setPassword("hashedPassword");

        String rawPassword = "wrongPassword";

        when(passwordEncoder.matches(rawPassword, "hashedPassword")).thenReturn(false);

        boolean matches = userService.checkPassword(user, rawPassword);

        verify(passwordEncoder, times(1)).matches(rawPassword, "hashedPassword");
        assertFalse(matches);
    }

    @Test
    void shouldFindUserByEmailWhenUserExists() {
        String email = "found@example.com";
        UserModel user = new UserModel();
        user.setId(2L);
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        UserModel foundUser = userService.findByEmail(email);

        verify(userRepository, times(1)).findByEmail(email);
        assertEquals(user, foundUser);
    }

    @Test
    void shouldReturnNullWhenFindByEmailAndUserDoesNotExist() {
        String email = "notfound@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        UserModel foundUser = userService.findByEmail(email);

        verify(userRepository, times(1)).findByEmail(email);
        assertNull(foundUser);
    }

    @Test
    void shouldCreateUserWhenValidRegistrationRequest() {
        RegistrationRequest form = new RegistrationRequest();
        form.setEmail("newuser@example.com");
        form.setPassword("securePassword");
        form.setFirstname("John");
        form.setLastname("Doe");

        RoleModel userRole = new RoleModel();
        userRole.setName("ROLE_USER");

        when(roleRepository.findByName("USER")).thenReturn(userRole);
        when(passwordEncoder.encode("securePassword")).thenReturn("encodedPassword");

        UserModel savedUser = new UserModel();
        savedUser.setId(3L);
        savedUser.setEmail("newuser@example.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setEnabled(true);
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setRoles(Collections.singleton(userRole));

        when(userRepository.save(any(UserModel.class))).thenReturn(savedUser);

        UserModel result = userService.createUser(form);

        ArgumentCaptor<UserModel> userCaptor = ArgumentCaptor.forClass(UserModel.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserModel capturedUser = userCaptor.getValue();
        assertEquals("newuser@example.com", capturedUser.getEmail());
        assertEquals("encodedPassword", capturedUser.getPassword());
        assertTrue(capturedUser.isEnabled());
        assertEquals("John", capturedUser.getFirstName());
        assertEquals("Doe", capturedUser.getLastName());
        assertEquals(Collections.singleton(userRole), capturedUser.getRoles());

        assertEquals(savedUser, result);
    }

    @Test
    void shouldCreateAnonymousUserWhenCalled() {
        String uniqueUsername = "anon-" + UUID.randomUUID() + "@user.com";

        RoleModel anonymousRole = new RoleModel();
        anonymousRole.setName("ROLE_ANONYMOUS");

        when(roleRepository.findByName("ANONYMOUS")).thenReturn(anonymousRole);

        UserModel savedUser = new UserModel();
        savedUser.setId(4L);
        savedUser.setEmail(uniqueUsername);
        savedUser.setAnonymous(true);
        savedUser.setEnabled(true);
        savedUser.setRoles(Collections.singleton(anonymousRole));

        when(userRepository.save(any(UserModel.class))).thenReturn(savedUser);

        UserModel result = userService.createAnonymousUser();

        ArgumentCaptor<UserModel> userCaptor = ArgumentCaptor.forClass(UserModel.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserModel capturedUser = userCaptor.getValue();
        assertNotNull(capturedUser.getEmail());
        assertTrue(capturedUser.getEmail().startsWith("anon-"));
        assertTrue(capturedUser.getEmail().endsWith("@user.com"));
        assertTrue(capturedUser.isAnonymous());
        assertTrue(capturedUser.isEnabled());
        assertEquals(Collections.singleton(anonymousRole), capturedUser.getRoles());

        assertEquals(savedUser, result);
    }

    @Test
    void shouldGetCurrentUserWhenUserIsAuthenticated() {
        String username = "currentuser@example.com";
        UserModel user = new UserModel();
        user.setId(5L);
        user.setEmail(username);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(username)).thenReturn(user);

        UserModel currentUser = userService.getCurrentUser();

        verify(userRepository, times(1)).findByEmail(username);
        assertEquals(user, currentUser);
    }

    @Test
    void shouldReturnNullWhenGetCurrentUserAndNoAuthenticatedUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("anonymousUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("anonymousUser")).thenReturn(null);

        UserModel currentUser = userService.getCurrentUser();

        verify(userRepository, times(1)).findByEmail("anonymousUser");
        assertNull(currentUser);
    }
}
