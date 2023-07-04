package com.example.mutsamarket.comment.commentDTO;

import com.example.mutsamarket.comment.Comment;
import lombok.Data;

@Data
public class ResponseCommentDto {
    private Long id;
    private String writer;
    private String content;
    private String reply;

    public static ResponseCommentDto fromEntity(Comment comment) {
        ResponseCommentDto dto = new ResponseCommentDto();
        dto.setId(comment.getId());
        dto.setWriter(comment.getWriter());
        dto.setContent(comment.getContent());
        dto.setReply(comment.getReply());
        return dto;
    }
}
