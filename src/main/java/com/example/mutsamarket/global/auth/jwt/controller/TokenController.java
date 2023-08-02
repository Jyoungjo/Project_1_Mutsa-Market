package com.example.mutsamarket.global.auth.jwt.controller;

import com.example.mutsamarket.global.auth.jwt.TokenService;
import com.example.mutsamarket.global.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/secure")
    public ResponseEntity<ResponseDto> check(HttpServletRequest request) {
        tokenService.checkToken(request);
        return ResponseEntity.ok(ResponseDto.getInstance("인증 성공"));
    }
}
