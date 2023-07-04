package com.example.mutsamarket.negotiation.dto;

import com.example.mutsamarket.negotiation.entity.Negotiation;
import lombok.Data;

@Data
public class ResponseNegotiationDto {
    private Long id;
    private Integer suggestedPrice;
    private String status;

    public static ResponseNegotiationDto fromEntity(Negotiation negotiation) {
        ResponseNegotiationDto dto = new ResponseNegotiationDto();
        dto.setId(negotiation.getId());
        dto.setStatus(negotiation.getStatus());
        dto.setSuggestedPrice(negotiation.getSuggestedPrice());
        return dto;
    }
}
