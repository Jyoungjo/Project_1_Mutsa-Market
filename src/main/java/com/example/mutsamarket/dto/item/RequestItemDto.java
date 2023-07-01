package com.example.mutsamarket.dto.item;

import com.example.mutsamarket.Entity.SalesItem;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestItemDto {
    @NotBlank
    private String title;

    @NotNull
    private String description;

    private String imageUrl;

    private Integer minPriceWanted;

    private String status;

    @Column(unique = true)
    @NotBlank
    private String writer;

    @NotBlank
    private String password;

    public static RequestItemDto fromEntity(SalesItem salesItem) {
        RequestItemDto dto = new RequestItemDto();
        dto.setTitle(salesItem.getTitle());
        dto.setDescription(salesItem.getDescription());
        dto.setImageUrl(salesItem.getImageUrl());
        dto.setMinPriceWanted(salesItem.getMinPriceWanted());
        dto.setWriter(salesItem.getWriter());
        dto.setPassword(salesItem.getPassword());
        return dto;
    }
}
