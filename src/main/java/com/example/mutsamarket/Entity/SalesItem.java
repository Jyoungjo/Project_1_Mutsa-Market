package com.example.mutsamarket.Entity;

import com.example.mutsamarket.dto.item.RequestItemDto;
import com.example.mutsamarket.exceptions.notmatch.NotMatchPasswordException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchWriterException;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "salesItem")
public class SalesItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private Integer minPriceWanted;

    private String status;

    @Column(unique = true)
    private String writer;

    private String password;

    public static SalesItem getInstance(RequestItemDto dto) {
        SalesItem newItem = new SalesItem();
        newItem.title = dto.getTitle();
        newItem.description = dto.getDescription();
        newItem.minPriceWanted = dto.getMinPriceWanted();
        newItem.status = ItemStatus.ON_SALE.getStatus();
        newItem.writer = dto.getWriter();
        newItem.password = dto.getPassword();
        return newItem;
    }

    public void updateInfo(RequestItemDto dto) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.minPriceWanted = dto.getMinPriceWanted();
    }

    public void checkAuthority(String writer, String password) {
        if (!this.writer.equals(writer)) {
            throw new NotMatchWriterException();
        }

        if (!this.password.equals(password)) {
            throw new NotMatchPasswordException();
        }
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
