package com.example.mutsamarket.exceptions.status400;

public class NotMatchPasswordException extends Status400Exception {
    public NotMatchPasswordException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
