package com.example.mutsamarket.exceptions.status404;

public class NotFoundNegotiationException extends Status404Exception {
    public NotFoundNegotiationException() {
        super("등록된 제안이 없습니다.");
    }
}
