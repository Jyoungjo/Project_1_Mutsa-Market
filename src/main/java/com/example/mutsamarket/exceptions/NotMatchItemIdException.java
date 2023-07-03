package com.example.mutsamarket.exceptions;

public class NotMatchItemIdException extends Status400Exception{
    public NotMatchItemIdException() {
        super("해당 게시글이 아닙니다.");
    }
}
