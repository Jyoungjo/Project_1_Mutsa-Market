package com.example.mutsamarket.user.entity;

import com.example.mutsamarket.comment.entity.Comment;
import com.example.mutsamarket.negotiation.entity.Negotiation;
import com.example.mutsamarket.salesitem.entity.SalesItem;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;

    private String phone;

    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SalesItem> salesItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Negotiation> negotiations;

    public void updateInfo(CustomUserDetails existingUser) {
        this.phone = existingUser.getPhone();
        this.email = existingUser.getEmail();
    }

    // 매핑 관련
    public void addSalesItem(SalesItem item) {
        if (!salesItems.contains(item)) salesItems.add(item);
    }

    public void deleteSalesItem(SalesItem item) {
        salesItems.remove(item);
    }

    public void addComment(Comment comment) {
        if (!comments.contains(comment)) comments.add(comment);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }

    public void addNegotiation(Negotiation negotiation) {
        if (!negotiations.contains(negotiation)) negotiations.add(negotiation);
    }

    public void deleteNegotiation(Negotiation negotiation) {
        negotiations.remove(negotiation);
    }
}
