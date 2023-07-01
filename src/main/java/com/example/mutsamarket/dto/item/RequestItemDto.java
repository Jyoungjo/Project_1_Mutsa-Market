package com.example.mutsamarket.dto.item;

import jakarta.persistence.Column;
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

    @Column(unique = true)
    @NotBlank
    private String writer;

    @NotBlank
    private String password;
}
