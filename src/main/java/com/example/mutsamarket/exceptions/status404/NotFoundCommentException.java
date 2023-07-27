package com.example.mutsamarket.exceptions.status404;

public class NotFoundCommentException extends Status404Exception {
    public NotFoundCommentException() {
        super("해당 댓글을 찾을 수 없습니다.");
    }
}
