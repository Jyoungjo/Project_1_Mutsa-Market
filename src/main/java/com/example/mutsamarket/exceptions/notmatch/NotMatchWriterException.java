package com.example.mutsamarket.exceptions.notmatch;

import com.example.mutsamarket.exceptions.Status400Exception;

public class NotMatchWriterException extends Status400Exception {
    public NotMatchWriterException() {
        super("작성자가 일치하지 않습니다.");
    }
}
