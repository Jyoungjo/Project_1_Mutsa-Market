package com.example.mutsamarket.domain.user.dto;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String username;
    private String password;
    private String passwordCheck;
    private String name;
    private String phone;
    private String email;
}
