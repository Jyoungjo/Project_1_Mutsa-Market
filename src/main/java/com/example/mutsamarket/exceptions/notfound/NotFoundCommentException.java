package com.example.mutsamarket.exceptions.notfound;

import com.example.mutsamarket.exceptions.Status404Exception;

public class NotFoundCommentException extends Status404Exception {
    public NotFoundCommentException() {
        super("해당 댓글을 찾을 수 없습니다.");
    }
}
