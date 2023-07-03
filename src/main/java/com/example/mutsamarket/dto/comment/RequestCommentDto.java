package com.example.mutsamarket.dto.comment;

import com.example.mutsamarket.Entity.Comment;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestCommentDto {
    private Long itemId;

    @Column(unique = true)
    @NotBlank
    private String writer;

    @NotBlank
    private String password;

    @NotBlank
    private String content;

    @NotBlank
    private String reply;

    public static RequestCommentDto fromEntity(Comment comment) {
        RequestCommentDto dto = new RequestCommentDto();
        dto.setItemId(comment.getItemId());
        dto.setWriter(comment.getWriter());
        dto.setPassword(comment.getPassword());
        dto.setContent(comment.getContent());
        dto.setReply(comment.getReply());
        return dto;
    }
}
