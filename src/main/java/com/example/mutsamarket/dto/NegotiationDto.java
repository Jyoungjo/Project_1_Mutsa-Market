package com.example.mutsamarket.dto;

import com.example.mutsamarket.Entity.Negotiation;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NegotiationDto {
    private Long itemId;

    @NotBlank
    private Integer suggestedPrice;

    private String status;

    @Column(unique = true)
    @NotBlank
    private String writer;

    @NotBlank
    private String password;

    public static NegotiationDto fromEntity(Negotiation negotiation) {
        NegotiationDto dto = new NegotiationDto();
        dto.setItemId(negotiation.getItemId());
        dto.setSuggestedPrice(negotiation.getSuggestedPrice());
        dto.setStatus(negotiation.getStatus());
        dto.setWriter(negotiation.getWriter());
        dto.setPassword(negotiation.getPassword());
        return dto;
    }
}
