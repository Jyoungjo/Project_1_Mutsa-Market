package com.example.mutsamarket.repository;

import com.example.mutsamarket.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
