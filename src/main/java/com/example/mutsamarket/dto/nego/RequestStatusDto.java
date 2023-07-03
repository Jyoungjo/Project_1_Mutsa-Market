package com.example.mutsamarket.dto.nego;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestStatusDto {
    @NotBlank
    private String writer;
    @NotBlank
    private String password;
    @NotBlank
    private String status;
}
