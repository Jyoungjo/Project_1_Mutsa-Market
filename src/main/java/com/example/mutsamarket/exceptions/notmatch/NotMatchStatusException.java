package com.example.mutsamarket.exceptions.notmatch;

import com.example.mutsamarket.exceptions.Status400Exception;

public class NotMatchStatusException extends Status400Exception {
    public NotMatchStatusException() {
        super("수락 상태가 아닙니다.");
    }
}
