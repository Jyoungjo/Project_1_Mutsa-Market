package com.example.mutsamarket.exceptions.notfound;

import com.example.mutsamarket.exceptions.Status404Exception;

public class NotFoundItemException extends Status404Exception {
    public NotFoundItemException() {
        super("등록되지 않은 물품입니다.");
    }
}
