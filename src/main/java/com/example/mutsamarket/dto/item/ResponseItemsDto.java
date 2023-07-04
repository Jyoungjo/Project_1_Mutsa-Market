package com.example.mutsamarket.dto.item;

import com.example.mutsamarket.Entity.ItemStatus;
import com.example.mutsamarket.Entity.SalesItem;
import lombok.Data;

@Data
public class ResponseItemsDto {
    private Long id;

    private String title;

    private String description;

    private Integer minPriceWanted;

    private String imageUrl;

    private ItemStatus status;

    public static ResponseItemsDto fromEntity(SalesItem salesItem) {
        ResponseItemsDto dto = new ResponseItemsDto();
        dto.setId(salesItem.getId());
        dto.setTitle(salesItem.getTitle());
        dto.setDescription(salesItem.getDescription());
        dto.setMinPriceWanted(salesItem.getMinPriceWanted());
        dto.setImageUrl(salesItem.getImageUrl());
        dto.setStatus(salesItem.getStatus());

        return dto;
    }
}
