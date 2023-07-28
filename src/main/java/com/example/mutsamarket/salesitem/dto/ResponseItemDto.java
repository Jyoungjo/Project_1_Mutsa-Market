package com.example.mutsamarket.salesitem.dto;

import com.example.mutsamarket.salesitem.entity.SalesItem;
import lombok.Data;

@Data
public class ResponseItemDto {
    private String title;

    private String username;

    private String description;

    private Integer minPriceWanted;

    private String status;

    public static ResponseItemDto fromEntity(SalesItem salesItem) {
        ResponseItemDto dto = new ResponseItemDto();
        dto.setTitle(salesItem.getTitle());
        dto.setUsername(salesItem.getUser().getUsername());
        dto.setDescription(salesItem.getDescription());
        dto.setMinPriceWanted(salesItem.getMinPriceWanted());
        dto.setStatus(salesItem.getStatus());
        return dto;
    }
}
