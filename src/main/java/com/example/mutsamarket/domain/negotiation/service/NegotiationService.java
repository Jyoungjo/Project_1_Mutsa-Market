package com.example.mutsamarket.domain.negotiation.service;

import com.example.mutsamarket.domain.negotiation.dto.RequestNegotiationDto;
import com.example.mutsamarket.domain.negotiation.dto.RequestNegotiationUserDto;
import com.example.mutsamarket.domain.negotiation.dto.ResponseNegotiationDto;
import com.example.mutsamarket.domain.negotiation.domain.Negotiation;
import com.example.mutsamarket.domain.negotiation.repository.NegotiationRepository;
import com.example.mutsamarket.domain.salesitem.exception.NotMatchItemException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NegotiationService {
    private final NegotiationRepository negotiationRepository;
    private final SalesItemRepository itemRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 구매 제안 등록
    public void addProposal(Long itemId, RequestNegotiationDto dto) {
        checkTokenInfo(dto.getUsername());

        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        UserEntity user = userRepository.findByUsername(dto.getUsername()).orElseThrow(NotFoundUserException::new);

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new NotMatchUserException();
        }

        Negotiation negotiation = Negotiation.getInstance(dto);
        negotiation.setSalesItem(item);
        negotiation.setUser(user);

        negotiationRepository.save(negotiation);
    }

    // 구매 제안 조회
    public Page<ResponseNegotiationDto> readProposal(
            Long itemId, String username, String password, Integer page
    ) {
        checkTokenInfo(username);

        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);
        Pageable pageable = PageRequest.of(page - 1, 25, Sort.by("id"));

        Page<Negotiation> negotiationPage;

        // 검증
        if (user.getUsername().equals(item.getUser().getUsername()) && passwordEncoder.matches(password, user.getPassword())) {
            negotiationPage = negotiationRepository.findAllBySalesItem(item, pageable);
            return negotiationPage.map(ResponseNegotiationDto::fromEntity);
        } else {
            negotiationPage = negotiationRepository.findAllBySalesItemIdAndUserId(itemId, user.getId(), pageable);
            return negotiationPage.map(ResponseNegotiationDto::fromEntity);
        }
    }

    // 가격 변경
    public void updatePrice(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        checkTokenInfo(dto.getUsername());

        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);

        // 검증
        checkItem(item, negotiation);
        checkAuthority(negotiation, dto.getUsername(), dto.getPassword());

        negotiation.updatePrice(dto);
        negotiationRepository.save(negotiation);
    }

    // 상태 변경
    public void updateStatus(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        checkTokenInfo(dto.getUsername());

        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);

        checkItem(item, negotiation);
        checkAuthority(item, dto.getUsername(), dto.getPassword());

        negotiation.setStatus(dto.getStatus());
        negotiationRepository.save(negotiation);
    }

    // 제안 확정
    public void acceptProposal(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        checkTokenInfo(dto.getUsername());

        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);

        checkItem(item, negotiation);
        checkAuthority(negotiation, dto.getUsername(), dto.getPassword());

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
    public void deleteProposal(Long itemId, Long proposalId, RequestNegotiationUserDto dto) {
        checkTokenInfo(dto.getUsername());

        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);

        checkItem(item, negotiation);
        checkAuthority(negotiation, dto.getUsername(), dto.getPassword());

        negotiation.getSalesItem().deleteNegotiation(negotiation);
        negotiation.getUser().deleteNegotiation(negotiation);
        negotiationRepository.deleteById(proposalId);
    }

    private void checkAuthority(Negotiation negotiation, String username, String password) {
        if (!negotiation.getUser().getUsername().equals(username) || !passwordEncoder.matches(password, negotiation.getUser().getPassword())) {
            throw new NotMatchUserException();
        }
    }

    private void checkAuthority(SalesItem item, String username, String password) {
        if (!item.getUser().getUsername().equals(username) || !passwordEncoder.matches(password, item.getUser().getPassword())) {
            throw new NotMatchUserException();
        }
    }

    private void checkItem(SalesItem salesItem, Negotiation negotiation) {
        if (!salesItem.getId().equals(negotiation.getSalesItem().getId())) {
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
