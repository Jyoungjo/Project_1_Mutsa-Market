package com.example.mutsamarket.domain.user.exception;

import com.example.mutsamarket.global.exception.Status404Exception;

public class NotFoundUserException extends Status404Exception {
    public NotFoundUserException() {
        super("해당 계정을 찾을 수 없습니다.");
    }
}
