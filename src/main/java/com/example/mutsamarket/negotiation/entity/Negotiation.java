package com.example.mutsamarket.negotiation.entity;

import com.example.mutsamarket.exceptions.notmatch.NotMatchItemException;
import com.example.mutsamarket.negotiation.dto.RequestNegotiationDto;
import com.example.mutsamarket.exceptions.notmatch.NotMatchPasswordException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchWriterException;
import com.example.mutsamarket.salesitem.entity.SalesItem;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "negotiation")
public class Negotiation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private SalesItem salesItem;

    private Integer suggestedPrice;

    private String status;

    @Column(unique = true)
    private String writer;

    private String password;

    public static Negotiation getInstance(RequestNegotiationDto dto) {
        Negotiation newNegotiation = new Negotiation();
        newNegotiation.suggestedPrice = dto.getSuggestedPrice();
        newNegotiation.status = NegotiationStatus.PROPOSED.getStatus();
        newNegotiation.writer = dto.getWriter();
        newNegotiation.password = dto.getPassword();
        return newNegotiation;
    }

    public void updatePrice(RequestNegotiationDto dto) {
        this.suggestedPrice = dto.getSuggestedPrice();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSalesItem(SalesItem item) {
        if (this.salesItem != null) this.salesItem.getNegotiations().remove(this);
        this.salesItem = item;
        item.addNegotiation(this);
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
