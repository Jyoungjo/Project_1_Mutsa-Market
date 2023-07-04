package com.example.mutsamarket.comment;

import com.example.mutsamarket.comment.commentDTO.RequestCommentDto;
import com.example.mutsamarket.comment.commentDTO.RequestReplyDto;
import com.example.mutsamarket.exceptions.notmatch.NotMatchPasswordException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchWriterException;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    @Column(unique = true)
    private String writer;

    private String password;

    private String content;

    private String reply;

    public static Comment getInstance(RequestCommentDto dto, Long itemId) {
        Comment newComment = new Comment();
        newComment.itemId = itemId;
        newComment.writer = dto.getWriter();
        newComment.password = dto.getPassword();
        newComment.content = dto.getContent();
        return newComment;
    }

    public void update(RequestCommentDto dto) {
        this.content = dto.getContent();
    }

    public void setReply(RequestReplyDto dto) {
        this.reply = dto.getReply();
    }

    public void checkAuthority(String writer, String password) {
        if (!this.writer.equals(writer)) {
            throw new NotMatchWriterException();
        }

        if (!this.password.equals(password)) {
            throw new NotMatchPasswordException();
        }
    }
}
