package com.example.mutsamarket.domain.user.exception;

import com.example.mutsamarket.global.exception.Status400Exception;

public class ExistentUserException extends Status400Exception {
    public ExistentUserException() {
        super("이미 존재하는 아이디입니다.");
    }
}
