package com.example.mutsamarket.negotiation;

import com.example.mutsamarket.negotiation.entity.Negotiation;
import com.example.mutsamarket.salesitem.entity.SalesItem;
import com.example.mutsamarket.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
    List<Negotiation> findAllBySalesItem(SalesItem item);

    Optional<Negotiation> findByUser(UserEntity user);

    Page<Negotiation> findAllBySalesItem(SalesItem item, Pageable pageable);

    Page<Negotiation> findAllBySalesItemAndUser(SalesItem item, UserEntity user, Pageable pageable);
}
