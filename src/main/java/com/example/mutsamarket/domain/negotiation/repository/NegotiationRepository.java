package com.example.mutsamarket.domain.negotiation.repository;

import com.example.mutsamarket.domain.negotiation.domain.Negotiation;
import com.example.mutsamarket.domain.salesitem.domain.SalesItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
    List<Negotiation> findAllBySalesItem(SalesItem item);

    Page<Negotiation> findAllBySalesItem(SalesItem item, Pageable pageable);

    Page<Negotiation> findAllBySalesItemIdAndUserUsername(Long itemId, String username, Pageable pageable);
}
