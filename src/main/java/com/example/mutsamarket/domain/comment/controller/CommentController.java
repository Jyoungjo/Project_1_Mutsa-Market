package com.example.mutsamarket.domain.comment.controller;

import com.example.mutsamarket.domain.comment.service.CommentService;
import com.example.mutsamarket.domain.comment.dto.RequestCommentDto;
import com.example.mutsamarket.domain.comment.dto.RequestCommentUserDto;
import com.example.mutsamarket.domain.comment.dto.RequestReplyDto;
import com.example.mutsamarket.domain.comment.dto.ResponseCommentDto;
import com.example.mutsamarket.global.response.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping
    public ResponseEntity<ResponseDto> create(
            @Valid @RequestBody RequestCommentDto dto,
            @PathVariable("itemId") Long itemId
    ) {
        commentService.addComment(dto, itemId);
        return ResponseEntity.ok(ResponseDto.getInstance("댓글이 등록되었습니다."));
    }

    // 댓글 조회
    @GetMapping
    public ResponseEntity<Page<ResponseCommentDto>> readAll(
            @PathVariable("itemId") Long itemId,
            @RequestParam(value = "page", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "25") Integer pageSize
    ) {
        return ResponseEntity.ok(commentService.readComments(itemId, pageNum, pageSize));
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<ResponseDto> update(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody RequestCommentDto dto
    ) {
        commentService.updateComment(commentId, itemId, dto);
        return ResponseEntity.ok(ResponseDto.getInstance("댓글이 수정되었습니다."));
    }

    // 댓글에 답글 등록
    @PutMapping("/{commentId}/reply")
    public ResponseEntity<ResponseDto> updateReply(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody RequestReplyDto dto
    ) {
        commentService.addReply(itemId, commentId, dto);
        return ResponseEntity.ok(ResponseDto.getInstance("댓글에 답변이 추가되었습니다."));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDto> delete(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody RequestCommentUserDto dto
            ) {
        commentService.deleteComment(itemId, commentId, dto);
        return ResponseEntity.ok(ResponseDto.getInstance("댓글을 삭제했습니다."));
    }
}
