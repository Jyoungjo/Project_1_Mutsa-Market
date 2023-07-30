package com.example.mutsamarket.domain.negotiation.exception;

import com.example.mutsamarket.global.exception.Status404Exception;

public class NotFoundNegotiationException extends Status404Exception {
    public NotFoundNegotiationException() {
        super("등록된 제안이 없습니다.");
    }
}
