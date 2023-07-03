package com.example.mutsamarket.dto.nego;

import lombok.Data;

@Data
public class ResponseNegotiationDto {
    private Long id;
    private Integer suggestedPrice;
    private String status;
}
