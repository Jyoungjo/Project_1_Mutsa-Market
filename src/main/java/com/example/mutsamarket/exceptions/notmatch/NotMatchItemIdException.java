package com.example.mutsamarket.exceptions.notmatch;

import com.example.mutsamarket.exceptions.Status400Exception;

public class NotMatchItemIdException extends Status400Exception {
    public NotMatchItemIdException() {
        super("해당 물품이 아닙니다.");
    }
}
