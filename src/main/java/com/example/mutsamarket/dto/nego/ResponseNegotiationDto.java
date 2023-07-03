package com.example.mutsamarket.dto.nego;

import com.example.mutsamarket.Entity.Negotiation;
import lombok.Data;

@Data
public class ResponseNegotiationDto {
    private Long id;
    private Integer suggestedPrice;
    private String status;

    public static ResponseNegotiationDto fromEntity(Negotiation negotiation) {
        ResponseNegotiationDto dto = new ResponseNegotiationDto();
        dto.setStatus(negotiation.getStatus());
        dto.setSuggestedPrice(negotiation.getSuggestedPrice());
        return dto;
    }
}
