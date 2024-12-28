package com.maup.practice.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.model.RoleModel;
import com.maup.practice.model.UserModel;
import com.maup.practice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class DBUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DBUserDetailsService userDetailsService;

    @Test
    void shouldLoadUserDetailsWhenUserExists() {
        String username = "existinguser@example.com";

        RoleModel role1 = new RoleModel();
        role1.setName("USER");

        RoleModel role2 = new RoleModel();
        role2.setName("ADMIN");

        UserModel user = new UserModel();
        user.setId(1L);
        user.setEmail(username);
        user.setPassword("hashedpassword");
        user.setEnabled(true);
        user.setRoles(Set.of(role1, role2));

        when(userRepository.findByEmail(username)).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        verify(userRepository, times(1)).findByEmail(username);
        assertEquals(username, userDetails.getUsername());
        assertEquals("hashedpassword", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertEquals(List.of("ROLE_ADMIN", "ROLE_USER"), userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .sorted()
                .toList());
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        String username = "nonexistent@example.com";

        when(userRepository.findByEmail(username)).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(username);
    }
}
