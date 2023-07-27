package com.example.mutsamarket.negotiation.entity;

import com.example.mutsamarket.negotiation.dto.RequestNegotiationDto;
import com.example.mutsamarket.salesitem.entity.SalesItem;
import com.example.mutsamarket.user.entity.UserEntity;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public static Negotiation getInstance(RequestNegotiationDto dto) {
        Negotiation newNegotiation = new Negotiation();
        newNegotiation.suggestedPrice = dto.getSuggestedPrice();
        newNegotiation.status = NegotiationStatus.PROPOSED.getStatus();
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

    public void setUser(UserEntity user) {
        if (this.user != null) this.user.getNegotiations().remove(this);
        this.user = user;
        user.addNegotiation(this);
    }
}
