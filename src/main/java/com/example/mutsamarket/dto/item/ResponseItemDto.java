package com.example.mutsamarket.dto.item;

import com.example.mutsamarket.Entity.ItemStatus;
import com.example.mutsamarket.Entity.SalesItem;
import lombok.Data;

@Data
public class ResponseItemDto {
    private String title;

    private String description;

    private Integer minPriceWanted;

    private ItemStatus status;

    public static ResponseItemDto fromEntity(SalesItem salesItem) {
        ResponseItemDto dto = new ResponseItemDto();
        dto.setTitle(salesItem.getTitle());
        dto.setDescription(salesItem.getDescription());
        dto.setMinPriceWanted(salesItem.getMinPriceWanted());
        dto.setStatus(salesItem.getStatus());
        return dto;
    }
}
