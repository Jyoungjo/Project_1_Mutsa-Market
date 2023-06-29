package com.example.mutsamarket.dto.item;

import com.example.mutsamarket.Entity.SalesItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResponsePageDto {
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private String description;

    @NotBlank
    private Integer minPriceWanted;

    private String imageUrl;

    @NotBlank
    private String status;

    public static ResponsePageDto fromEntity(SalesItem salesItem) {
        ResponsePageDto dto = new ResponsePageDto();
        dto.setId(salesItem.getId());
        dto.setTitle(salesItem.getTitle());
        dto.setDescription(salesItem.getDescription());
        dto.setMinPriceWanted(salesItem.getMinPriceWanted());
        dto.setImageUrl(salesItem.getImageUrl());
        dto.setStatus(salesItem.getStatus());

        return dto;
    }
}
