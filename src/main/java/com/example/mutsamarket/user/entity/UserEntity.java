package com.example.mutsamarket.user.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;

    private String phone;

    private String email;

    public void updateInfo(CustomUserDetails existingUser) {
        this.phone = existingUser.getPhone();
        this.email = existingUser.getEmail();
    }
}
