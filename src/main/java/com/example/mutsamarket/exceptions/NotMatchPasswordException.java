package com.example.mutsamarket.exceptions;

public class NotMatchPasswordException extends Status403Exception{
    public NotMatchPasswordException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
