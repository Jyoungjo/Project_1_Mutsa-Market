package com.example.mutsamarket.comment.entity;

import com.example.mutsamarket.comment.dto.RequestCommentDto;
import com.example.mutsamarket.comment.dto.RequestReplyDto;
import com.example.mutsamarket.exceptions.notmatch.NotMatchItemException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchPasswordException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchWriterException;
import com.example.mutsamarket.salesitem.entity.SalesItem;
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

    @ManyToOne
    @JoinColumn(name = "item_id")
    private SalesItem salesItem;

    @Column(unique = true)
    private String writer;

    private String password;

    private String content;

    private String reply;

    public static Comment getInstance(RequestCommentDto dto) {
        Comment newComment = new Comment();
        newComment.writer = dto.getWriter();
        newComment.password = dto.getPassword();
        newComment.content = dto.getContent();
        return newComment;
    }

    public void setSalesItem(SalesItem item) {
        if (this.salesItem != null) this.salesItem.getComments().remove(this);
        this.salesItem = item;
        item.addComment(this);
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

    public void checkItem(Long itemId) {
        if (!salesItem.getId().equals(itemId)) {
            throw new NotMatchItemException();
        }
    }
}
