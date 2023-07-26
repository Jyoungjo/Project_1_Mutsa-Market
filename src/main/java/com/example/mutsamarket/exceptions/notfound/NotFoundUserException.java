package com.example.mutsamarket.exceptions.notfound;

import com.example.mutsamarket.exceptions.Status404Exception;

public class NotFoundUserException extends Status404Exception {
    public NotFoundUserException() {
        super("해당 계정을 찾을 수 없습니다.");
    }
}
