package com.example.mutsamarket.exceptions.status400;

public class ExistentUserException extends Status400Exception {
    public ExistentUserException() {
        super("이미 존재하는 아이디입니다.");
    }
}
