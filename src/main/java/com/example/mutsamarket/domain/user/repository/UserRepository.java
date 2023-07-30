package com.example.mutsamarket.domain.user.repository;

import com.example.mutsamarket.domain.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByPassword(String password);

    Boolean existsByUsername(String username);
}
