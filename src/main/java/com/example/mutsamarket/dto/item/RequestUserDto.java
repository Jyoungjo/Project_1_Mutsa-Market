package com.example.mutsamarket.dto.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestUserDto {
    @NotBlank
    private String writer;
    @NotBlank
    private String password;
}
