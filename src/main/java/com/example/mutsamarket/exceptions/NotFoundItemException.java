package com.example.mutsamarket.exceptions;

public class NotFoundItemException extends Status404Exception{
    public NotFoundItemException() {
        super("등록되지 않은 아이템입니다.");
    }
}
