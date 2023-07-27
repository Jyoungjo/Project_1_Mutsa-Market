package com.example.mutsamarket.exceptions.notmatch;

import com.example.mutsamarket.exceptions.Status400Exception;

public class NotMatchItemException extends Status400Exception {
    public NotMatchItemException() {
        super("해당 물품이 아닙니다.");
    }
}
