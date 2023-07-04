package com.example.mutsamarket.service;

import com.example.mutsamarket.Entity.*;
import com.example.mutsamarket.dto.nego.RequestNegotiationDto;
import com.example.mutsamarket.dto.nego.RequestNegotiationUserDto;
import com.example.mutsamarket.dto.nego.ResponseNegotiationDto;
import com.example.mutsamarket.exceptions.notfound.NotFoundItemException;
import com.example.mutsamarket.exceptions.notfound.NotFoundNegotiationException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchItemIdException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchStatusException;
import com.example.mutsamarket.repository.NegotiationRepository;
import com.example.mutsamarket.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NegotiationService {
    private final NegotiationRepository negotiationRepository;
    private final SalesItemRepository itemRepository;

    // 구매 제안 등록
    public void addProposal(Long itemId, RequestNegotiationDto dto) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundItemException();
        }
        negotiationRepository.save(Negotiation.getInstance(itemId, dto));
    }

    // 구매 제안 조회
    public Page<ResponseNegotiationDto> readProposal(
            Long itemId, String writer, String password, Integer page
    ) {
        Pageable pageable = PageRequest.of(page - 1, 25, Sort.by("id"));

        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);

        Page<Negotiation> negotiationPage;

        if (writer.equals(item.getWriter()) && password.equals(item.getPassword())) {
            negotiationPage = negotiationRepository.findAllByItemId(itemId, pageable);
            return negotiationPage.map(ResponseNegotiationDto::fromEntity);
        }

        Negotiation negotiation = negotiationRepository.findByWriter(writer).orElseThrow(NotFoundNegotiationException::new);

        if (writer.equals(negotiation.getWriter()) && password.equals(negotiation.getPassword())) {
            negotiationPage = negotiationRepository.findAllByItemIdAndWriterLikeAndPasswordLike(itemId, writer, password, pageable);
            return negotiationPage.map(ResponseNegotiationDto::fromEntity);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // 가격 변경
    public void updatePrice(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);

        checkItemId(itemId, negotiation);
        negotiation.checkAuthority(dto.getWriter(), dto.getPassword());

        negotiation.updatePrice(dto);
        negotiationRepository.save(negotiation);
    }

    // 상태 변경
    public void updateStatus(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);

        checkItemId(itemId, negotiation);

        item.checkAuthority(dto.getWriter(), dto.getPassword());

        negotiation.setStatus(dto.getStatus());
        negotiationRepository.save(negotiation);
    }

    // 제안 확정
    public void acceptProposal(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);

        checkItemId(itemId, negotiation);
        negotiation.checkAuthority(dto.getWriter(), dto.getPassword());

        if (negotiation.getStatus().equals(NegotiationStatus.ACCEPTED.getStatus())) {

            negotiation.setStatus(dto.getStatus());
            negotiationRepository.save(negotiation);

            item.setStatus(ItemStatus.SOLD.getStatus());
            itemRepository.save(item);

            for (Negotiation nego : negotiationRepository.findAllByItemId(itemId)) {
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

        checkItemId(itemId, negotiation);
        negotiation.checkAuthority(dto.getWriter(), dto.getPassword());

        negotiationRepository.deleteById(proposalId);
    }

    private void checkItemId(Long itemId, Negotiation negotiation) {
        if (!itemId.equals(negotiation.getItemId())) {
            throw new NotMatchItemIdException();
        }
    }
}
