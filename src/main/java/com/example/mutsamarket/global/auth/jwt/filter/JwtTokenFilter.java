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
        if (request.getMethod().equals("GET") && isSkipUrl(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new JwtAuthenticationException();
        }

        String token = authorization.split(" ")[1];
        log.info("token: {}", token);

        if (jwtTokenUtils.validate(token)) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            String username = jwtTokenUtils.parseClaims(token).getSubject();

            UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

            AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, token, new ArrayList<>()
            );

            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);
            log.info("인증 성공");
        } else {
            log.warn("인증 실패");
            throw new JwtAuthenticationException();
        }

        filterChain.doFilter(request, response);
    }

    private boolean isSkipUrl(String url) {
        return ((url.startsWith("/items") && !url.contains("/proposal")) || (!url.contains("/products/new") && !url.contains("/update")));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/users", "/auth", "/main", "/products"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::contains);
    }
}
