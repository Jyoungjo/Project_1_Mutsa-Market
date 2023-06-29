package com.example.mutsamarket.repository;

import com.example.mutsamarket.Entity.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {
}
