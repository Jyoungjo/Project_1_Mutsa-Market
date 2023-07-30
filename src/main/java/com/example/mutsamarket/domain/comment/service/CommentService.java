package com.example.mutsamarket.domain.comment.service;

import com.example.mutsamarket.domain.comment.repository.CommentRepository;
import com.example.mutsamarket.domain.comment.domain.Comment;
import com.example.mutsamarket.domain.comment.dto.RequestCommentDto;
import com.example.mutsamarket.domain.comment.dto.RequestCommentUserDto;
import com.example.mutsamarket.domain.comment.dto.RequestReplyDto;
import com.example.mutsamarket.domain.comment.dto.ResponseCommentDto;
import com.example.mutsamarket.domain.comment.exception.NotFoundCommentException;
import com.example.mutsamarket.domain.salesitem.exception.NotMatchItemException;
import com.example.mutsamarket.domain.user.exception.NotFoundUserException;
import com.example.mutsamarket.domain.user.exception.NotMatchUserException;
import com.example.mutsamarket.domain.salesitem.domain.SalesItem;
import com.example.mutsamarket.domain.salesitem.exception.NotFoundItemException;
import com.example.mutsamarket.domain.salesitem.repository.SalesItemRepository;
import com.example.mutsamarket.domain.user.repository.UserRepository;
import com.example.mutsamarket.domain.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final SalesItemRepository itemRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 댓글 등록
    public void addComment(RequestCommentDto dto, Long itemId) {
        checkTokenInfo(dto.getUsername());

        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        UserEntity user = userRepository.findByUsername(dto.getUsername()).orElseThrow(NotFoundUserException::new);

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new NotMatchUserException();
        }

        Comment comment = Comment.getInstance(dto);
        comment.setSalesItem(item);
        comment.setUser(user);

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
        checkTokenInfo(dto.getUsername());

        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);

        checkItem(item, comment);
        checkAuthority(comment, dto.getUsername(), dto.getPassword());

        comment.update(dto);
        commentRepository.save(comment);
    }

    // 해당 댓글에 답글 등록
    public void addReply(Long itemId, Long commentId, RequestReplyDto dto) {
        checkTokenInfo(dto.getUsername());

        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);

        checkItem(item, comment);
        checkAuthority(item, dto.getUsername(), dto.getPassword());

        comment.setReply(dto);
        commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long itemId, Long commentId, RequestCommentUserDto dto) {
        checkTokenInfo(dto.getUsername());

        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);

        checkItem(item, comment);
        checkAuthority(comment, dto.getUsername(), dto.getPassword());

        comment.getSalesItem().deleteComment(comment);
        comment.getUser().deleteComment(comment);
        commentRepository.deleteById(commentId);
    }

    // 사용자 인증 메소드
    private void checkAuthority(Comment comment, String username, String password) {
        if (!comment.getUser().getUsername().equals(username) || !passwordEncoder.matches(password, comment.getUser().getPassword())) {
            throw new NotMatchUserException();
        }
    }

    private void checkAuthority(SalesItem item, String username, String password) {
        if (!item.getUser().getUsername().equals(username) || !passwordEncoder.matches(password, item.getUser().getPassword())) {
            throw new NotMatchUserException();
        }
    }

    private void checkItem(SalesItem salesItem, Comment comment) {
        if (!salesItem.getId().equals(comment.getSalesItem().getId())) {
            throw new NotMatchItemException();
        }
    }

    private void checkTokenInfo(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals(username)) {
            throw new NotMatchUserException();
        }
    }
}
