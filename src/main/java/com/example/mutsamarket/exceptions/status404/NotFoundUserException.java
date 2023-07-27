package com.example.mutsamarket.exceptions.status404;

public class NotFoundUserException extends Status404Exception {
    public NotFoundUserException() {
        super("해당 계정을 찾을 수 없습니다.");
    }
}
