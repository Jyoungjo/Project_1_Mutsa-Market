package com.example.mutsamarket.dto.nego;

import com.example.mutsamarket.Entity.Negotiation;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestNegotiationDto {
    private Long itemId;

    private Integer suggestedPrice;

    private String status;

    @NotBlank
    private String writer;

    @NotBlank
    private String password;

    public static RequestNegotiationDto fromEntity(Negotiation negotiation) {
        RequestNegotiationDto dto = new RequestNegotiationDto();
        dto.setItemId(negotiation.getItemId());
        dto.setSuggestedPrice(negotiation.getSuggestedPrice());
        dto.setStatus(negotiation.getStatus());
        dto.setWriter(negotiation.getWriter());
        dto.setPassword(negotiation.getPassword());
        return dto;
    }
}
