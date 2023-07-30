package com.example.mutsamarket.domain.salesitem.repository;

import com.example.mutsamarket.domain.salesitem.domain.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {
}
