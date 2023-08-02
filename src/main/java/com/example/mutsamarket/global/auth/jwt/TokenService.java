package com.example.mutsamarket.global.auth.jwt;

import com.example.mutsamarket.global.auth.jwt.exception.JwtAuthenticationException;
import com.example.mutsamarket.global.auth.jwt.provider.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenUtils jwtUtils;

    public void checkToken(HttpServletRequest request) {
        String requestHeader = request.getHeader("Authorization");
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            String token = requestHeader.split(" ")[1];
            String username = jwtUtils.parseClaims(token).getSubject();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (!authentication.getName().equals(username)) {
                throw new JwtAuthenticationException();
            }
        }
    }
}
