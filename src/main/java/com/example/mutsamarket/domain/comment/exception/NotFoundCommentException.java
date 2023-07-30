package com.example.mutsamarket.domain.comment.exception;

import com.example.mutsamarket.global.exception.Status404Exception;

public class NotFoundCommentException extends Status404Exception {
    public NotFoundCommentException() {
        super("해당 댓글을 찾을 수 없습니다.");
    }
}
