package com.example.mutsamarket.comment;

import com.example.mutsamarket.comment.entity.Comment;
import com.example.mutsamarket.salesitem.entity.SalesItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllBySalesItem(SalesItem item, Pageable pageable);
}
