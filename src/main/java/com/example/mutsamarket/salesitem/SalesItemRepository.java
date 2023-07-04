package com.example.mutsamarket.salesitem;

import com.example.mutsamarket.salesitem.entity.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {
}
