package com.example.mutsamarket.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestReplyDto {
    @NotBlank
    private String username;

    @NotBlank
    private String reply;
}
