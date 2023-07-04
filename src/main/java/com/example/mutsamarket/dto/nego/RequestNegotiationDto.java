package com.example.mutsamarket.dto.nego;

import com.example.mutsamarket.Entity.NegotiationStatus;
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
