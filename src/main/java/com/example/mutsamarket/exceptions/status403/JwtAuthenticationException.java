package com.example.mutsamarket.exceptions.status403;

public class JwtAuthenticationException extends Status403Exception {
    public JwtAuthenticationException() {
        super("다시 로그인하여 주시기 바랍니다.");
    }
}
