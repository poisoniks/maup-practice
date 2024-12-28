package com.maup.practice.config;

import com.maup.practice.filter.AnonymousUserAuthenticationFilter;
import com.maup.practice.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.maup.practice.util.Constants.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager,
            JwtAuthFilter jwtAuthFilter,
            AnonymousUserAuthenticationFilter anonymousUserAuthenticationFilter
    ) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/", "/js/**", "/css/**", "/images/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/product/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/profile").hasAnyRole(ROLE_USER, ROLE_MANAGER, ROLE_ADMIN)

                        .requestMatchers(HttpMethod.GET, "/settings").hasAnyRole(ROLE_USER, ROLE_MANAGER, ROLE_ADMIN)

                        .requestMatchers(HttpMethod.GET, "/checkout").hasAnyRole(ROLE_ANONYMOUS, ROLE_USER, ROLE_MANAGER, ROLE_ADMIN)

                        .requestMatchers(HttpMethod.GET, "/orders").hasAnyRole(ROLE_USER, ROLE_MANAGER, ROLE_ADMIN)

                        .requestMatchers(HttpMethod.GET, "/backoffice").hasAnyRole(ROLE_MANAGER, ROLE_ADMIN)

                        .requestMatchers("/console").hasRole(ROLE_ADMIN)

                        .requestMatchers("/api/basket/**").hasAnyRole(ROLE_ANONYMOUS, ROLE_USER, ROLE_MANAGER, ROLE_ADMIN)

                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        .requestMatchers("/api/profile/**").hasAnyRole(ROLE_ANONYMOUS, ROLE_USER, ROLE_MANAGER, ROLE_ADMIN)

                        .requestMatchers("/api/address/**").hasAnyRole(ROLE_ANONYMOUS, ROLE_USER, ROLE_MANAGER, ROLE_ADMIN)

                        .requestMatchers("/api/checkout/**").hasAnyRole(ROLE_ANONYMOUS, ROLE_USER, ROLE_MANAGER, ROLE_ADMIN)

                        .requestMatchers("/api/orders/**").hasAnyRole(ROLE_USER, ROLE_MANAGER, ROLE_ADMIN)

                        .requestMatchers("/api/backoffice/**").hasAnyRole(ROLE_MANAGER, ROLE_ADMIN)

                        .requestMatchers("/actuator/**").hasRole(ROLE_ADMIN)

                        .anyRequest().denyAll()
                )
                .authenticationManager(authenticationManager)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(anonymousUserAuthenticationFilter, JwtAuthFilter.class)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
