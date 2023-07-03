package com.example.mutsamarket.dto.comment;

import com.example.mutsamarket.Entity.Comment;
import jakarta.persistence.Column;
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
