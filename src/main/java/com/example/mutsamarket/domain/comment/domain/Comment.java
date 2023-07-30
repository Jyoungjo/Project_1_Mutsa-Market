package com.example.mutsamarket.domain.comment.domain;

import com.example.mutsamarket.domain.comment.dto.RequestCommentDto;
import com.example.mutsamarket.domain.comment.dto.RequestReplyDto;
import com.example.mutsamarket.domain.salesitem.domain.SalesItem;
import com.example.mutsamarket.domain.user.domain.UserEntity;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String content;

    private String reply;

    public static Comment getInstance(RequestCommentDto dto) {
        Comment newComment = new Comment();
        newComment.content = dto.getContent();
        return newComment;
    }

    // 매핑 관련
    public void setSalesItem(SalesItem item) {
        if (this.salesItem != null) this.salesItem.getComments().remove(this);
        this.salesItem = item;
        item.addComment(this);
    }

    public void setUser(UserEntity user) {
        if (this.user != null) this.user.getComments().remove(this);
        this.user = user;
        user.addComment(this);
    }

    public void update(RequestCommentDto dto) {
        this.content = dto.getContent();
    }

    public void setReply(RequestReplyDto dto) {
        this.reply = dto.getReply();
    }
}
