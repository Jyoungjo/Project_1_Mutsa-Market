package com.example.mutsamarket.exceptions.status400;

public class NotMatchUserException extends Status400Exception {
    public NotMatchUserException() {
        super("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
}
