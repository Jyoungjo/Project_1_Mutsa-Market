package com.example.mutsamarket.domain.salesitem.dto;

import com.example.mutsamarket.domain.salesitem.domain.SalesItem;
import lombok.Data;

@Data
public class ResponseItemsDto {
    private Long id;

    private String title;

    private String username;

    private String description;

    private Integer minPriceWanted;

    private String imageUrl;

    private String status;

    public static ResponseItemsDto fromEntity(SalesItem salesItem) {
        ResponseItemsDto dto = new ResponseItemsDto();
        dto.setId(salesItem.getId());
        dto.setTitle(salesItem.getTitle());
        dto.setUsername(salesItem.getUser().getUsername());
        dto.setDescription(salesItem.getDescription());
        dto.setMinPriceWanted(salesItem.getMinPriceWanted());
        dto.setImageUrl(salesItem.getImageUrl());
        dto.setStatus(salesItem.getStatus());

        return dto;
    }
}
