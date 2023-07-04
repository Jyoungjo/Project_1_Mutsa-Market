package com.example.mutsamarket.negotiation.dto;

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
}
