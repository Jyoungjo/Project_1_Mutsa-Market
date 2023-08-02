package com.example.mutsamarket.domain.negotiation.dto;

import lombok.Data;

@Data
public class RequestNegotiationDto {
    private Long itemId;

    private Integer suggestedPrice;

    private String status;
}
