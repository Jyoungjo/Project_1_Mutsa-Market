package com.example.mutsamarket.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestReplyDto {
    @NotBlank
    private String writer;
    @NotBlank
    private String password;
    @NotBlank
    private String reply;
}
