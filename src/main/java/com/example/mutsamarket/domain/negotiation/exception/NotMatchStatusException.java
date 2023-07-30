package com.example.mutsamarket.domain.negotiation.exception;

import com.example.mutsamarket.global.exception.Status400Exception;

public class NotMatchStatusException extends Status400Exception {
    public NotMatchStatusException() {
        super("수락 상태가 아닙니다.");
    }
}
