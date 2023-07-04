package com.example.mutsamarket.negotiation;

import com.example.mutsamarket.negotiation.entity.Negotiation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
    List<Negotiation> findAllByItemId(Long itemId);

    Optional<Negotiation> findByWriter(String writer);

    Page<Negotiation> findAllByItemId(Long itemId, Pageable pageable);

    Page<Negotiation> findAllByItemIdAndWriterLikeAndPasswordLike(Long itemId, String writer, String password,Pageable pageable);
}
