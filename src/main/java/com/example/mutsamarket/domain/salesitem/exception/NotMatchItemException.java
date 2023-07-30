package com.example.mutsamarket.domain.salesitem.exception;

import com.example.mutsamarket.global.exception.Status400Exception;

public class NotMatchItemException extends Status400Exception {
    public NotMatchItemException() {
        super("해당 물품이 아닙니다.");
    }
}
