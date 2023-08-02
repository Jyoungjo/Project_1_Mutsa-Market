package com.example.mutsamarket.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestCommentDto {
    private Long itemId;

    @NotBlank
    private String content;

    private String reply;
}
