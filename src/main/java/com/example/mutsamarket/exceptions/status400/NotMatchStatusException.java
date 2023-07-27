package com.example.mutsamarket.exceptions.status400;

public class NotMatchStatusException extends Status400Exception {
    public NotMatchStatusException() {
        super("수락 상태가 아닙니다.");
    }
}
