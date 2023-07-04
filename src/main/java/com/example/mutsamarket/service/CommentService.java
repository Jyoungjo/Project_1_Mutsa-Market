package com.example.mutsamarket.service;

import com.example.mutsamarket.Entity.Comment;
import com.example.mutsamarket.Entity.SalesItem;
import com.example.mutsamarket.dto.comment.RequestCommentDto;
import com.example.mutsamarket.dto.comment.RequestCommentUserDto;
import com.example.mutsamarket.dto.comment.RequestReplyDto;
import com.example.mutsamarket.dto.comment.ResponseCommentDto;
import com.example.mutsamarket.exceptions.notfound.NotFoundCommentException;
import com.example.mutsamarket.exceptions.notfound.NotFoundItemException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchItemIdException;
import com.example.mutsamarket.repository.CommentRepository;
import com.example.mutsamarket.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final SalesItemRepository itemRepository;

    // 댓글 등록
    public void addComment(RequestCommentDto dto, Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundItemException();
        }
        commentRepository.save(Comment.getInstance(dto, itemId));
    }

    // 댓글 조회
    public Page<ResponseCommentDto> readComments(Long itemId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        Page<Comment> commentPage = commentRepository.findAllByItemId(itemId, pageable);

        return commentPage.map(ResponseCommentDto::fromEntity);
    }

    // 댓글 수정
    public void updateComment(Long commentId, Long itemId, RequestCommentDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);

        checkItemId(itemId, comment);
        comment.checkAuthority(dto.getWriter(), dto.getPassword());

        comment.update(dto);
        commentRepository.save(comment);
    }

    // 해당 댓글에 답글 등록
    public void addReply(Long itemId, Long commentId, RequestReplyDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        checkItemId(itemId, comment);

        SalesItem salesItem = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        salesItem.checkAuthority(dto.getWriter(), dto.getPassword());

        comment.setReply(dto);
        commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long itemId, Long commentId, RequestCommentUserDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        checkItemId(itemId, comment);
        comment.checkAuthority(dto.getWriter(), dto.getPassword());

        commentRepository.deleteById(commentId);
    }

    private void checkItemId(Long itemId, Comment comment) {
        if (!itemId.equals(comment.getItemId())) {
            throw new NotMatchItemIdException();
        }
    }
}
