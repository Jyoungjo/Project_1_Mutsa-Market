package com.example.mutsamarket.negotiation;

import com.example.mutsamarket.negotiation.entity.Negotiation;
import com.example.mutsamarket.negotiation.entity.NegotiationStatus;
import com.example.mutsamarket.negotiation.dto.RequestNegotiationDto;
import com.example.mutsamarket.negotiation.dto.RequestNegotiationUserDto;
import com.example.mutsamarket.negotiation.dto.ResponseNegotiationDto;
import com.example.mutsamarket.exceptions.notfound.NotFoundItemException;
import com.example.mutsamarket.exceptions.notfound.NotFoundNegotiationException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchStatusException;
import com.example.mutsamarket.salesitem.SalesItemRepository;
import com.example.mutsamarket.salesitem.entity.ItemStatus;
import com.example.mutsamarket.salesitem.entity.SalesItem;
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

    // 구매 제안 등록
    public void addProposal(Long itemId, RequestNegotiationDto dto) {
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Negotiation negotiation = Negotiation.getInstance(dto);
        negotiation.setSalesItem(item);
        negotiationRepository.save(negotiation);
    }

    // 구매 제안 조회
    public Page<ResponseNegotiationDto> readProposal(
            Long itemId, String writer, String password, Integer page
    ) {
        Pageable pageable = PageRequest.of(page - 1, 25, Sort.by("id"));

        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);

        Page<Negotiation> negotiationPage;

        // 검증
        // 물품 등록자
        if (writer.equals(item.getWriter()) && password.equals(item.getPassword())) {
            negotiationPage = negotiationRepository.findAllBySalesItem(item, pageable);
            return negotiationPage.map(ResponseNegotiationDto::fromEntity);
        }

        Negotiation negotiation = negotiationRepository.findByWriter(writer).orElseThrow(NotFoundNegotiationException::new);

        // 제안 등록자
        if (writer.equals(negotiation.getWriter()) && password.equals(negotiation.getPassword())) {
            negotiationPage = negotiationRepository.findAllBySalesItemAndWriterLikeAndPasswordLike(item, writer, password, pageable);
            return negotiationPage.map(ResponseNegotiationDto::fromEntity);
        }

        throw new NotFoundNegotiationException();
    }

    // 가격 변경
    public void updatePrice(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);

        // 검증
        negotiation.checkItem(itemId);
        negotiation.checkAuthority(dto.getWriter(), dto.getPassword());

        negotiation.updatePrice(dto);
        negotiationRepository.save(negotiation);
    }

    // 상태 변경
    public void updateStatus(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);

        negotiation.checkItem(itemId);

        item.checkAuthority(dto.getWriter(), dto.getPassword());

        negotiation.setStatus(dto.getStatus());
        negotiationRepository.save(negotiation);
    }

    // 제안 확정
    public void acceptProposal(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        SalesItem item = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NotFoundNegotiationException::new);

        negotiation.checkItem(itemId);
        negotiation.checkAuthority(dto.getWriter(), dto.getPassword());

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

        negotiation.checkItem(itemId);
        negotiation.checkAuthority(dto.getWriter(), dto.getPassword());

        negotiation.getSalesItem().deleteNegotiation(negotiation);
        negotiationRepository.deleteById(proposalId);
    }
}
