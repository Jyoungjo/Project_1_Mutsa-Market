package com.example.mutsamarket.domain.comment.repository;

import com.example.mutsamarket.domain.comment.domain.Comment;
import com.example.mutsamarket.domain.salesitem.domain.SalesItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllBySalesItem(SalesItem item, Pageable pageable);
}
