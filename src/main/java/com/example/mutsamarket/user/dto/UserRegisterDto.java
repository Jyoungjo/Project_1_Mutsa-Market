package com.example.mutsamarket.user.dto;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String username;
    private String password;
    private String passwordCheck;
}
