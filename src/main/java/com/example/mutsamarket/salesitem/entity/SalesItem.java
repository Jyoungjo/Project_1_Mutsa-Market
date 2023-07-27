package com.example.mutsamarket.salesitem.entity;

import com.example.mutsamarket.comment.entity.Comment;
import com.example.mutsamarket.negotiation.entity.Negotiation;
import com.example.mutsamarket.salesitem.dto.RequestItemDto;
import com.example.mutsamarket.exceptions.notmatch.NotMatchPasswordException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchWriterException;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "sales_item")
public class SalesItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private Integer minPriceWanted;

    private String status;

    @Column(unique = true)
    private String writer;

    private String password;

    @OneToMany(mappedBy = "salesItem", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "salesItem", cascade = CascadeType.ALL)
    private List<Negotiation> negotiations;

    public static SalesItem getInstance(RequestItemDto dto) {
        SalesItem newItem = new SalesItem();
        newItem.title = dto.getTitle();
        newItem.description = dto.getDescription();
        newItem.minPriceWanted = dto.getMinPriceWanted();
        newItem.status = ItemStatus.ON_SALE.getStatus();
        newItem.writer = dto.getWriter();
        newItem.password = dto.getPassword();
        return newItem;
    }

    public void updateInfo(RequestItemDto dto) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.minPriceWanted = dto.getMinPriceWanted();
    }

    public void checkAuthority(String writer, String password) {
        if (!this.writer.equals(writer)) {
            throw new NotMatchWriterException();
        }

        if (!this.password.equals(password)) {
            throw new NotMatchPasswordException();
        }
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 매핑 관련
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
