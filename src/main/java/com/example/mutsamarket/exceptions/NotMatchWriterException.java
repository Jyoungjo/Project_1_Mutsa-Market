package com.example.mutsamarket.exceptions;

public class NotMatchWriterException extends Status403Exception{
    public NotMatchWriterException() {
        super("작성자가 일치하지 않습니다.");
    }
}
