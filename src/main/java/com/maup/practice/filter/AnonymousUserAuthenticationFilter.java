package com.maup.practice.filter;

import com.maup.practice.facade.AuthenticationFacade;
import com.maup.practice.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AnonymousUserAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationFacade authenticationFacade;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AnonymousUserAuthenticationFilter(AuthenticationFacade authenticationFacade, JWTService jwtService, UserDetailsService userDetailsService) {
        this.authenticationFacade = authenticationFacade;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = authenticationFacade.anonymousLogin();
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            Cookie jwtCookie = jwtService.generateJWTCookie(token);
            response.addCookie(jwtCookie);
        }
        filterChain.doFilter(request, response);
    }
}
