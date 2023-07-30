package com.example.mutsamarket.domain.user.exception;

import com.example.mutsamarket.global.exception.Status400Exception;

public class NotMatchUserException extends Status400Exception {
    public NotMatchUserException() {
        super("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
}
