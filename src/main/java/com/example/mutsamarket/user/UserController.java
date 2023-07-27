package com.example.mutsamarket.user;

import com.example.mutsamarket.exceptions.status400.NotMatchPasswordException;
import com.example.mutsamarket.exceptions.status403.NotMatchUserException;
import com.example.mutsamarket.response.ResponseDto;
import com.example.mutsamarket.user.dto.TokenDto;
import com.example.mutsamarket.user.dto.UserRegisterDto;
import com.example.mutsamarket.user.dto.UserRequestDto;
import com.example.mutsamarket.user.entity.CustomUserDetails;
import com.example.mutsamarket.user.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserRequestDto dto) {
        // 검증
        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());
        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            throw new NotMatchUserException();
        }

        TokenDto token = new TokenDto();
        token.setToken(jwtTokenUtils.generateToken(userDetails));

        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody UserRegisterDto dto) {
        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new NotMatchPasswordException();
        }
        manager.createUser(CustomUserDetails.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .build());

        return ResponseEntity.ok(ResponseDto.getInstance("회원가입이 완료되었습니다."));
    }
}
