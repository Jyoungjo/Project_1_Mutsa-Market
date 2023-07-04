package com.example.mutsamarket.negotiation.entity;

import com.example.mutsamarket.negotiation.dto.RequestNegotiationDto;
import com.example.mutsamarket.exceptions.notmatch.NotMatchPasswordException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchWriterException;
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

    private Long itemId;

    private Integer suggestedPrice;

    private String status;

    @Column(unique = true)
    private String writer;

    private String password;

    public static Negotiation getInstance(Long itemId, RequestNegotiationDto dto) {
        Negotiation newNegotiation = new Negotiation();
        newNegotiation.itemId = itemId;
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

    public void checkAuthority(String writer, String password) {
        if (!this.writer.equals(writer)) {
            throw new NotMatchWriterException();
        }

        if (!this.password.equals(password)) {
            throw new NotMatchPasswordException();
        }
    }
}
