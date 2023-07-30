package com.example.mutsamarket.domain.salesitem.exception;

import com.example.mutsamarket.global.exception.Status404Exception;

public class NotFoundItemException extends Status404Exception {
    public NotFoundItemException() {
        super("등록되지 않은 물품입니다.");
    }
}
