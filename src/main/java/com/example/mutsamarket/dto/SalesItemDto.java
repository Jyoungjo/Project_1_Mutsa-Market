package com.example.mutsamarket.dto;

import com.example.mutsamarket.Entity.SalesItem;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SalesItemDto {
    @NotBlank
    private String title;

    @NotNull
    private String description;

    private String imageUrl;

    @NotBlank
    private Integer minPriceWanted;

    private String status;

    @Column(unique = true)
    @NotBlank
    private String writer;

    @NotBlank
    private String password;

    public static SalesItemDto fromEntity(SalesItem salesItem) {
        SalesItemDto dto = new SalesItemDto();
        dto.setTitle(salesItem.getTitle());
        dto.setDescription(salesItem.getDescription());
        dto.setImageUrl(salesItem.getImageUrl());
        dto.setMinPriceWanted(salesItem.getMinPriceWanted());
        dto.setWriter(salesItem.getWriter());
        dto.setPassword(salesItem.getPassword());
        return dto;
    }
}
