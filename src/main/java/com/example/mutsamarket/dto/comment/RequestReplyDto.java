package com.example.mutsamarket.dto.comment;

import lombok.Data;

@Data
public class RequestReplyDto {
    private String writer;
    private String password;
    private String reply;
}
