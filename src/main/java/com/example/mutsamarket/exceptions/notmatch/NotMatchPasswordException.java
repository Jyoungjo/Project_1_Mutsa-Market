package com.example.mutsamarket.exceptions.notmatch;

import com.example.mutsamarket.exceptions.Status400Exception;

public class NotMatchPasswordException extends Status400Exception {
    public NotMatchPasswordException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
