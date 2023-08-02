package com.example.mutsamarket.global.auth.jwt.filter;

import com.example.mutsamarket.global.auth.jwt.exception.JwtAuthenticationException;
import com.example.mutsamarket.global.auth.jwt.provider.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager userDetailsManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.split(" ")[1];
        if (!jwtTokenUtils.validate(token)) {
            throw new JwtAuthenticationException();
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        String username = jwtTokenUtils.parseClaims(token).getSubject();

        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

        AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, token, new ArrayList<>()
        );

        context.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }
}
