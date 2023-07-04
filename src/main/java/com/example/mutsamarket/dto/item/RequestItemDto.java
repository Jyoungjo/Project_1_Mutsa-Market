package com.example.mutsamarket.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RequestItemDto {
    @NotBlank
    private String title;

    @NotEmpty
    private String description;

    private String imageUrl;

    private Integer minPriceWanted;

    private String status;

    @NotBlank
    private String writer;

    @NotBlank
    private String password;
}
