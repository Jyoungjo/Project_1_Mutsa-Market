package com.example.mutsamarket.service;

import com.example.mutsamarket.Entity.Comment;
import com.example.mutsamarket.Entity.SalesItem;
import com.example.mutsamarket.dto.ResponseDto;
import com.example.mutsamarket.dto.comment.RequestCommentDto;
import com.example.mutsamarket.dto.comment.RequestCommentUserDto;
import com.example.mutsamarket.dto.comment.RequestReplyDto;
import com.example.mutsamarket.dto.comment.ResponseCommentDto;
import com.example.mutsamarket.exceptions.notfound.NotFoundCommentException;
import com.example.mutsamarket.exceptions.notfound.NotFoundItemException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchItemIdException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchPasswordException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchWriterException;
import com.example.mutsamarket.repository.CommentRepository;
import com.example.mutsamarket.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final SalesItemRepository itemRepository;

    // 댓글 등록
    public ResponseDto addComment(RequestCommentDto dto, Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundItemException();
        }

        Comment newComment = new Comment();
        newComment.setItemId(itemId);
        newComment.setWriter(dto.getWriter());
        newComment.setPassword(dto.getPassword());
        newComment.setContent(dto.getContent());
        newComment.setReply(dto.getReply());
        commentRepository.save(newComment);
        return new ResponseDto("댓글이 등록되었습니다.");
    }

    // 댓글 조회
    public Page<ResponseCommentDto> readComments(Long itemId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        Page<Comment> commentPage = commentRepository.findAllByItemId(itemId, pageable);

        return commentPage.map(ResponseCommentDto::fromEntity);
    }

    // 댓글 수정
    public ResponseDto updateComment(Long commentId, Long itemId, RequestCommentDto dto) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new NotFoundCommentException();
        }

        Comment comment = optionalComment.get();

        if (!itemId.equals(comment.getItemId())) {
            throw new NotMatchItemIdException();
        }

        if (!dto.getWriter().equals(comment.getWriter())) {
            throw new NotMatchWriterException();
        }

        if (!dto.getPassword().equals(comment.getPassword())) {
            throw new NotMatchPasswordException();
        }

        comment.setContent(dto.getContent());
        commentRepository.save(comment);

        return new ResponseDto("댓글이 수정되었습니다.");
    }

    // 해당 댓글에 답글 등록
    public ResponseDto addReply(Long itemId, Long commentId, RequestReplyDto dto) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        // 댓글 유무 체크
        if (optionalComment.isEmpty()) {
            throw new NotFoundCommentException();
        }

        Comment comment = optionalComment.get();

        // 물품 아이디와 댓글의 물품 아이디 체크
        if (!itemId.equals(comment.getItemId())) {
            throw new NotFoundItemException();
        }

        Optional<SalesItem> optionalItem = itemRepository.findById(itemId);

        // 물품 존재 여부 체크
        if (optionalItem.isEmpty()) {
            throw new NotFoundItemException();
        }

        SalesItem salesItem = optionalItem.get();

        // 답변의 작성자와 물품 등록자 같은지 체크
        if (!dto.getWriter().equals(salesItem.getWriter())) {
            throw new NotMatchWriterException();
        }

        // 답변의 비밀번호와 물품 등록 시 비밀번호 같은지 체크
        if (!dto.getPassword().equals(salesItem.getPassword())) {
            throw new NotMatchPasswordException();
        }

        comment.setReply(dto.getReply());
        commentRepository.save(comment);

        return new ResponseDto("댓글에 답변이 추가되었습니다.");
    }

    // 댓글 삭제
    public ResponseDto deleteComment(Long itemId, Long commentId, RequestCommentUserDto dto) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new NotFoundCommentException();
        }

        Comment comment = optionalComment.get();

        if (!itemId.equals(comment.getItemId())) {
            throw new NotMatchItemIdException();
        }

        if (!dto.getWriter().equals(comment.getWriter())) {
            throw new NotMatchWriterException();
        }

        if (!dto.getPassword().equals(comment.getPassword())) {
            throw new NotMatchPasswordException();
        }

        commentRepository.deleteById(commentId);
        return new ResponseDto("댓글을 삭제했습니다");
    }
}
