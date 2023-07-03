package com.example.mutsamarket.controller;

import com.example.mutsamarket.dto.ResponseDto;
import com.example.mutsamarket.dto.comment.RequestCommentDto;
import com.example.mutsamarket.dto.comment.RequestCommentUserDto;
import com.example.mutsamarket.dto.comment.RequestReplyDto;
import com.example.mutsamarket.dto.comment.ResponseCommentDto;
import com.example.mutsamarket.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping
    public ResponseDto create(
            @Valid @RequestBody RequestCommentDto dto,
            @PathVariable("itemId") Long itemId
    ) {
        return commentService.addComment(dto, itemId);
    }

    // 댓글 조회
    @GetMapping
    public Page<ResponseCommentDto> readAll(
            @PathVariable("itemId") Long itemId,
            @RequestParam(value = "page", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "25") Integer pageSize
    ) {
        return commentService.readComments(itemId, pageNum, pageSize);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseDto update(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody RequestCommentDto dto
    ) {
        return commentService.updateComment(commentId, itemId, dto);
    }

    // 댓글에 답글 등록
    @PutMapping("/{commentId}/reply")
    public ResponseDto updateReply(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody RequestReplyDto dto
    ) {
        return commentService.addReply(itemId, commentId, dto);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseDto delete(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody RequestCommentUserDto dto
            ) {
        return commentService.deleteComment(itemId, commentId, dto);
    }
}
