package com.example.mutsamarket.domain.negotiation.service;

import com.example.mutsamarket.domain.negotiation.dto.RequestNegotiationDto;
import com.example.mutsamarket.domain.negotiation.dto.ResponseNegotiationDto;
import com.example.mutsamarket.domain.negotiation.domain.Negotiation;
import com.example.mutsamarket.domain.negotiation.repository.NegotiationRepository;
import com.example.mutsamarket.domain.user.exception.NotMatchUserException;
import com.example.mutsamarket.domain.user.exception.NotFoundUserException;
import com.example.mutsamarket.domain.negotiation.domain.NegotiationStatus;
import com.example.mutsamarket.domain.salesitem.exception.NotFoundItemException;
import com.example.mutsamarket.domain.negotiation.exception.NotFoundNegotiationException;
import com.example.mutsamarket.domain.negotiation.exception.NotMatchStatusException;
import com.example.mutsamarket.domain.salesitem.repository.SalesItemRepository;
import com.example.mutsamarket.domain.salesitem.domain.ItemStatus;
import com.example.mutsamarket.domain.salesitem.domain.SalesItem;
import com.example.mutsamarket.domain.user.repository.UserRepository;
import com.example.mutsamarket.domain.user.domain.UserEntity;
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
public class NegotiationService {
    private final NegotiationRepository negotiationRepository;
    private final SalesItemRepository itemRepository;
    private final UserRepository userRepository;

    // 구매 제안 등록
    public void addProposal(Long itemId, RequestNegotiationDto dto, String username) {
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);

        Negotiation negotiation = Negotiation.getInstance(dto);
        negotiation.setSalesItem(item);
        negotiation.setUser(user);

        negotiationRepository.save(negotiation);
    }

    // 구매 제안 조회
    public Page<ResponseNegotiationDto> readProposal(
            Long itemId, String username, Integer pageNum, Integer pageSize
    ) {
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));

        Page<Negotiation> negotiationPage;

        // 검증
        // 1. 물품 등록자
        if (item.getUser().getUsername().equals(user.getUsername())) {
            negotiationPage = negotiationRepository.findAllBySalesItem(item, pageable);
            return negotiationPage.map(ResponseNegotiationDto::fromEntity);
        }

        // 2. 제안 등록자
        negotiationPage = negotiationRepository.findAllBySalesItemIdAndUserUsername(itemId, user.getUsername(), pageable);
        return negotiationPage.map(ResponseNegotiationDto::fromEntity);
    }

    // 가격 변경
    public void updatePrice(Long itemId, Long proposalId, RequestNegotiationDto dto, String username) {
        checkItem(itemId);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);

        // 검증
        checkAuthority(negotiation, user);

        negotiation.updatePrice(dto);
        negotiationRepository.save(negotiation);
    }

    // 상태 변경
    public void updateStatus(Long itemId, Long proposalId, RequestNegotiationDto dto, String username) {
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);

        checkAuthority(item, user);

        negotiation.setStatus(dto.getStatus());
        negotiationRepository.save(negotiation);
    }

    // 제안 확정
    public void acceptProposal(Long itemId, Long proposalId, RequestNegotiationDto dto, String username) {
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);

        checkAuthority(negotiation, user);

        if (negotiation.getStatus().equals(NegotiationStatus.ACCEPTED.getStatus())) {

            negotiation.setStatus(dto.getStatus());
            negotiationRepository.save(negotiation);

            item.setStatus(ItemStatus.SOLD.getStatus());
            itemRepository.save(item);

            for (Negotiation nego : negotiationRepository.findAllBySalesItem(item)) {
                if (!nego.getStatus().equals(NegotiationStatus.CONFIRMED.getStatus())) {
                    nego.setStatus(NegotiationStatus.REJECTED.getStatus());
                    negotiationRepository.save(nego);
                }
            }
        }

        else {
            throw new NotMatchStatusException();
        }
    }

    // 구매 제안 삭제
    public void deleteProposal(Long itemId, Long proposalId, String username) {
        checkItem(itemId);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);

        checkAuthority(negotiation, user);

        negotiation.getSalesItem().deleteNegotiation(negotiation);
        negotiation.getUser().deleteNegotiation(negotiation);
        negotiationRepository.deleteById(proposalId);
    }

    private void checkAuthority(Negotiation negotiation, UserEntity user) {
        if (!negotiation.getUser().getUsername().equals(user.getUsername())) {
            throw new NotMatchUserException();
        }
    }

    private void checkAuthority(SalesItem item, UserEntity user) {
        if (!item.getUser().getUsername().equals(user.getUsername())) {
            throw new NotMatchUserException();
        }
    }

    private void checkItem(Long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundItemException();
        }
    }
}
