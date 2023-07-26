package com.example.mutsamarket.user.jwt;

import com.example.mutsamarket.user.dto.TokenDto;
import com.example.mutsamarket.user.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("token")
public class TokenController {
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping("/issue")
    public TokenDto issueJwt(@RequestBody UserRequestDto dto) {
        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());
        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        TokenDto response = new TokenDto();
        response.setToken(jwtTokenUtils.generateToken(userDetails));
        return response;
    }

    @PostMapping("/secured")
    public String checkSecure() {
        log.info(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()
        );
        return "success";
    }
}
