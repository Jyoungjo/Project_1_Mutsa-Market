package com.example.mutsamarket.negotiation;

import com.example.mutsamarket.exceptions.status400.NotMatchItemException;
import com.example.mutsamarket.exceptions.status403.NotMatchUserException;
import com.example.mutsamarket.exceptions.status404.NotFoundUserException;
import com.example.mutsamarket.negotiation.entity.Negotiation;
import com.example.mutsamarket.negotiation.entity.NegotiationStatus;
import com.example.mutsamarket.negotiation.dto.RequestNegotiationDto;
import com.example.mutsamarket.negotiation.dto.RequestNegotiationUserDto;
import com.example.mutsamarket.negotiation.dto.ResponseNegotiationDto;
import com.example.mutsamarket.exceptions.status404.NotFoundItemException;
import com.example.mutsamarket.exceptions.status404.NotFoundNegotiationException;
import com.example.mutsamarket.exceptions.status400.NotMatchStatusException;
import com.example.mutsamarket.salesitem.SalesItemRepository;
import com.example.mutsamarket.salesitem.entity.ItemStatus;
import com.example.mutsamarket.salesitem.entity.SalesItem;
import com.example.mutsamarket.user.UserRepository;
import com.example.mutsamarket.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        UserEntity user = userRepository.findByUsername(dto.getUsername()).orElseThrow(NotFoundUserException::new);

        Negotiation negotiation = Negotiation.getInstance(dto);
        negotiation.setSalesItem(item);
        negotiation.setUser(user);

        negotiationRepository.save(negotiation);
    }

    // 구매 제안 조회
    public Page<ResponseNegotiationDto> readProposal(
            Long itemId, String username, String password, Integer page
    ) {
        Pageable pageable = PageRequest.of(page - 1, 25, Sort.by("id"));

        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new); // 유저1
        UserEntity user = userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new); // 유저2

        Page<Negotiation> negotiationPage;

        // 검증
        if (user.getUsername().equals(item.getUser().getUsername()) && passwordEncoder.matches(password, user.getPassword())) {
            negotiationPage = negotiationRepository.findAllBySalesItem(item, pageable);
            return negotiationPage.map(ResponseNegotiationDto::fromEntity);
        }

        Negotiation negotiation = negotiationRepository.findByUser(user).orElseThrow(NotFoundNegotiationException::new); // 유저 2

        if (negotiation.getUser().getUsername().equals(username) && passwordEncoder.matches(password, user.getPassword())) {
            negotiationPage = negotiationRepository.findAllBySalesItemAndUser(item, user, pageable);
            return negotiationPage.map(ResponseNegotiationDto::fromEntity);
        }

        throw new NotFoundNegotiationException();
    }

    // 가격 변경
    public void updatePrice(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);

        // 검증
        checkItem(item, itemId);
        checkAuthority(negotiation, dto.getUsername(), dto.getPassword());

        negotiation.updatePrice(dto);
        negotiationRepository.save(negotiation);
    }

    // 상태 변경
    public void updateStatus(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);

        checkItem(item, itemId);
        checkAuthority(item, dto.getUsername(), dto.getPassword());

        negotiation.setStatus(dto.getStatus());
        negotiationRepository.save(negotiation);
    }

    // 제안 확정
    public void acceptProposal(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);

        checkItem(item, itemId);
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
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);

        checkItem(item, itemId);
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

    private void checkItem(SalesItem salesItem, Long itemId) {
        if (!salesItem.getId().equals(itemId)) {
            throw new NotMatchItemException();
        }
    }
}
