package com.example.mutsamarket.domain.user.exception;

import com.example.mutsamarket.global.exception.Status400Exception;

public class NotMatchPasswordException extends Status400Exception {
    public NotMatchPasswordException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
