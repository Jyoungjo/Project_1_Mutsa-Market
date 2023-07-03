package com.example.mutsamarket.dto.comment;

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
