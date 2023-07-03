package com.example.mutsamarket.dto.comment;

import com.example.mutsamarket.Entity.Comment;
import lombok.Data;

@Data
public class ResponseCommentDto {
    private Long id;
    private String content;
    private String reply;

    public static ResponseCommentDto fromEntity(Comment comment) {
        ResponseCommentDto dto = new ResponseCommentDto();
        dto.setContent(comment.getContent());
        dto.setReply(comment.getReply());
        return dto;
    }
}
