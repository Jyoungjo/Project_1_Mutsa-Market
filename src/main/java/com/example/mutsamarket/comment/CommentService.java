package com.example.mutsamarket.comment;

import com.example.mutsamarket.comment.entity.Comment;
import com.example.mutsamarket.exceptions.status400.NotMatchItemException;
import com.example.mutsamarket.exceptions.status400.NotMatchUserException;
import com.example.mutsamarket.salesitem.entity.SalesItem;
import com.example.mutsamarket.comment.dto.RequestCommentDto;
import com.example.mutsamarket.comment.dto.RequestCommentUserDto;
import com.example.mutsamarket.comment.dto.RequestReplyDto;
import com.example.mutsamarket.comment.dto.ResponseCommentDto;
import com.example.mutsamarket.exceptions.status404.NotFoundCommentException;
import com.example.mutsamarket.exceptions.status404.NotFoundItemException;
import com.example.mutsamarket.salesitem.SalesItemRepository;
import com.example.mutsamarket.user.UserRepository;
import com.example.mutsamarket.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        UserEntity user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

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
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);

        checkItem(item, itemId);
        checkAuthority(comment, dto.getUsername(), dto.getPassword());

        comment.update(dto);
        commentRepository.save(comment);
    }

    // 해당 댓글에 답글 등록
    public void addReply(Long itemId, Long commentId, RequestReplyDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);

        checkItem(item, itemId);
        checkAuthority(item, dto.getUsername(), dto.getPassword());

        comment.setReply(dto);
        commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long itemId, Long commentId, RequestCommentUserDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);

        checkItem(item, itemId);
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

    private void checkItem(SalesItem salesItem, Long itemId) {
        if (!salesItem.getId().equals(itemId)) {
            throw new NotMatchItemException();
        }
    }
}
