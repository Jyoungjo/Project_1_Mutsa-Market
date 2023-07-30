package com.example.mutsamarket.global.auth.jwt.exception;

import com.example.mutsamarket.global.exception.Status403Exception;

public class JwtAuthenticationException extends Status403Exception {
    public JwtAuthenticationException() {
        super("다시 로그인하여 주시기 바랍니다.");
    }
}
