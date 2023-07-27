package com.example.mutsamarket.exceptions.status400;

public class NotMatchItemException extends Status400Exception {
    public NotMatchItemException() {
        super("해당 물품이 아닙니다.");
    }
}
