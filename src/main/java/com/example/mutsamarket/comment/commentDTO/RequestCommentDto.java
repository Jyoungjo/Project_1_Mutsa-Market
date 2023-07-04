package com.example.mutsamarket.comment.commentDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestCommentDto {
    private Long itemId;

    @NotBlank
    private String writer;

    @NotBlank
    private String password;

    @NotBlank
    private String content;

    private String reply;
}
