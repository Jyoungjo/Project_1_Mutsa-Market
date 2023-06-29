package com.example.mutsamarket.dto;

import com.example.mutsamarket.Entity.Comment;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDto {
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

    public static CommentDto fromEntity(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setItemId(comment.getItemId());
        dto.setWriter(comment.getWriter());
        dto.setPassword(comment.getPassword());
        dto.setContent(comment.getContent());
        dto.setReply(comment.getReply());
        return dto;
    }
}
