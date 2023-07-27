package com.example.mutsamarket.comment;

import com.example.mutsamarket.comment.entity.Comment;
import com.example.mutsamarket.salesitem.entity.SalesItem;
import com.example.mutsamarket.comment.dto.RequestCommentDto;
import com.example.mutsamarket.comment.dto.RequestCommentUserDto;
import com.example.mutsamarket.comment.dto.RequestReplyDto;
import com.example.mutsamarket.comment.dto.ResponseCommentDto;
import com.example.mutsamarket.exceptions.notfound.NotFoundCommentException;
import com.example.mutsamarket.exceptions.notfound.NotFoundItemException;
import com.example.mutsamarket.salesitem.SalesItemRepository;
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
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Comment comment = Comment.getInstance(dto);
        comment.setSalesItem(item);
        commentRepository.save(comment);
    }

    // 댓글 조회
    public Page<ResponseCommentDto> readComments(Long itemId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Page<Comment> commentPage = commentRepository.findAllBySalesItem(item, pageable);

        return commentPage.map(ResponseCommentDto::fromEntity);
    }

    // 댓글 수정
    public void updateComment(Long commentId, Long itemId, RequestCommentDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);

        // 검증
        comment.checkItem(itemId); // 판매 물품이 해당 판매 물품인지
        comment.checkAuthority(dto.getWriter(), dto.getPassword()); // 작성자 본인 확인

        comment.update(dto);
        commentRepository.save(comment);
    }

    // 해당 댓글에 답글 등록
    public void addReply(Long itemId, Long commentId, RequestReplyDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);

        // 검증
        comment.checkItem(itemId);
        comment.getSalesItem().checkAuthority(dto.getWriter(), dto.getPassword());

        comment.setReply(dto);
        commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long itemId, Long commentId, RequestCommentUserDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);

        // 검증
        comment.checkItem(itemId);
        comment.checkAuthority(dto.getWriter(), dto.getPassword());

        comment.getSalesItem().deleteComment(comment);
        commentRepository.deleteById(commentId);
    }
}
