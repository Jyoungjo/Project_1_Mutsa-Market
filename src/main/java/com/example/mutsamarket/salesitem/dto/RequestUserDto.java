package com.example.mutsamarket.salesitem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestUserDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
