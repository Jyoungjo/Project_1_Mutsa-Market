package com.example.mutsamarket.domain.comment.dto;

import com.example.mutsamarket.domain.comment.domain.Comment;
import lombok.Data;

@Data
public class ResponseCommentDto {
    private Long id;
    private String username;
    private String content;
    private String reply;

    public static ResponseCommentDto fromEntity(Comment comment) {
        ResponseCommentDto dto = new ResponseCommentDto();
        dto.setId(comment.getId());
        dto.setUsername(comment.getUser().getUsername());
        dto.setContent(comment.getContent());
        dto.setReply(comment.getReply());
        return dto;
    }
}
