package com.example.mutsamarket.dto.item;

import com.example.mutsamarket.Entity.SalesItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResponseItemDto {
    @NotBlank
    private String title;

    @NotNull
    private String description;

    @NotBlank
    private Integer minPriceWanted;

    private String status;

    public static ResponseItemDto fromEntity(SalesItem salesItem) {
        ResponseItemDto dto = new ResponseItemDto();
        dto.setTitle(salesItem.getTitle());
        dto.setDescription(salesItem.getDescription());
        dto.setMinPriceWanted(salesItem.getMinPriceWanted());
        dto.setStatus(salesItem.getStatus());
        return dto;
    }
}
