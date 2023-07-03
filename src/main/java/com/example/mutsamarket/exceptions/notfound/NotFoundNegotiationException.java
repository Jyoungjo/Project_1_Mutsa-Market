package com.example.mutsamarket.exceptions.notfound;

import com.example.mutsamarket.exceptions.Status404Exception;

public class NotFoundNegotiationException extends Status404Exception {
    public NotFoundNegotiationException() {
        super("등록된 제안이 없습니다.");
    }
}
