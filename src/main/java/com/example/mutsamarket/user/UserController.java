package com.example.mutsamarket.user;

import com.example.mutsamarket.ResponseDto;
import com.example.mutsamarket.user.dto.UserRegisterDto;
import com.example.mutsamarket.user.dto.UserRequestDto;
import com.example.mutsamarket.user.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/users")
public class UserController {
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody UserRequestDto dto) {
        manager.loadUserByUsername(dto.getUsername());
        return ResponseEntity.ok(ResponseDto.getInstance("로그인이 완료되었습니다."));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody UserRegisterDto dto) {
        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        manager.createUser(CustomUserDetails.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build());

        return ResponseEntity.ok(ResponseDto.getInstance("회원가입이 완료되었습니다."));
    }
}
