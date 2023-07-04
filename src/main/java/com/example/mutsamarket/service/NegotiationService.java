package com.example.mutsamarket.service;

import com.example.mutsamarket.Entity.Negotiation;
import com.example.mutsamarket.Entity.SalesItem;
import com.example.mutsamarket.dto.ResponseDto;
import com.example.mutsamarket.dto.nego.RequestNegotiationDto;
import com.example.mutsamarket.dto.nego.RequestNegotiationUserDto;
import com.example.mutsamarket.dto.nego.ResponseNegotiationDto;
import com.example.mutsamarket.exceptions.notfound.NotFoundItemException;
import com.example.mutsamarket.exceptions.notfound.NotFoundNegotiationException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchItemIdException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchPasswordException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchStatusException;
import com.example.mutsamarket.exceptions.notmatch.NotMatchWriterException;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NegotiationService {
    private final NegotiationRepository negotiationRepository;
    private final SalesItemRepository itemRepository;

    // 구매 제안 등록
    public ResponseDto addProposal(Long itemId, RequestNegotiationDto dto) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundItemException();
        }

        Negotiation newNegotiation = new Negotiation();
        newNegotiation.setItemId(itemId);
        newNegotiation.setSuggestedPrice(dto.getSuggestedPrice());
        newNegotiation.setStatus("제안");
        newNegotiation.setWriter(dto.getWriter());
        newNegotiation.setPassword(dto.getPassword());
        negotiationRepository.save(newNegotiation);
        return new ResponseDto("구매 제안이 등록되었습니다.");
    }

    // 구매 제안 조회
    public Page<ResponseNegotiationDto> readProposal(
            Long itemId, String writer, String password, Integer page
    ) {
        Pageable pageable = PageRequest.of(page - 1, 25, Sort.by("id"));

        Optional<SalesItem> optionalSalesItem = itemRepository.findById(itemId);
        if (optionalSalesItem.isEmpty()) {
            throw new NotFoundItemException();
        }

        SalesItem item = optionalSalesItem.get();

        Page<Negotiation> negotiationPage;

        // 물품 주인
        if (writer.equals(item.getWriter())) {
            if (password.equals(item.getPassword())) {
                negotiationPage = negotiationRepository.findAllByItemId(itemId, pageable);
                return negotiationPage.map(ResponseNegotiationDto::fromEntity);
            } else {
                throw new NotMatchPasswordException();
            }
        }

        Optional<Negotiation> optionalNegotiation = negotiationRepository.findByWriter(writer);
        if (optionalNegotiation.isEmpty()) {
            throw new NotFoundNegotiationException();
        }

        Negotiation negotiation = optionalNegotiation.get();

        // 제안 등록자
        if (writer.equals(negotiation.getWriter())){
            if (password.equals(negotiation.getPassword())) {
                negotiationPage = negotiationRepository.findAllByItemIdAndWriterLikeAndPasswordLike(itemId, writer, password, pageable);
                return negotiationPage.map(ResponseNegotiationDto::fromEntity);
            }
            else {
                throw new NotMatchPasswordException();
            }
        } else throw new NotMatchWriterException();
    }

    // 구매 제안 수정
    public ResponseDto updateProposal(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        // 가격 변경
        if (dto.getStatus() == null) {
            return updateProposalPrice(itemId, proposalId, dto);
        }

        // 구매 제안 상태 변경
        if (dto.getSuggestedPrice() == null) {
            return updateProposalStatus(itemId, proposalId, dto);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    // 구매 제안 삭제
    public ResponseDto deleteProposal(Long itemId, Long proposalId, RequestNegotiationUserDto dto) {
        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(proposalId);

        if (optionalNegotiation.isEmpty()) {
            throw new NotFoundNegotiationException();
        }

        Negotiation negotiation = optionalNegotiation.get();

        if (!itemId.equals(negotiation.getItemId())) {
            throw new NotFoundItemException();
        }

        if (!dto.getWriter().equals(negotiation.getWriter())) {
            throw new NotMatchWriterException();
        }

        if (!dto.getPassword().equals(negotiation.getPassword())) {
            throw new NotMatchPasswordException();
        }

        negotiationRepository.deleteById(proposalId);
        return new ResponseDto("제안을 삭제했습니다.");
    }

    private ResponseDto updateProposalStatus(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        Optional<SalesItem> optionalItem = itemRepository.findById(itemId);

        if (optionalItem.isEmpty()) {
            throw new NotFoundItemException();
        }

        SalesItem item = optionalItem.get();

        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(proposalId);

        if (optionalNegotiation.isEmpty()) {
            throw new NotFoundNegotiationException();
        }

        Negotiation negotiation = optionalNegotiation.get();

        if (!itemId.equals(negotiation.getItemId())) {
            throw new NotMatchItemIdException();
        }

        ResponseDto responseDto;

        // 수락 or 거절 상태 변경하는 경우
        if (dto.getStatus().equals("수락") || dto.getStatus().equals("거절")) {
            if (!dto.getWriter().equals(item.getWriter())) {
                throw new NotMatchWriterException();
            }

            if (!dto.getPassword().equals(item.getPassword())) {
                throw new NotMatchPasswordException();
            }

            negotiation.setStatus(dto.getStatus());
            negotiationRepository.save(negotiation);
            responseDto = new ResponseDto("제안의 상태가 변경되었습니다.");
            return responseDto;
        }

        // 확정을 지으려는 경우
        else if (dto.getStatus().equals("확정")) {
            if (!dto.getWriter().equals(negotiation.getWriter())) {
                throw new NotMatchWriterException();
            }

            if (!dto.getPassword().equals(negotiation.getPassword())) {
                throw new NotMatchPasswordException();
            }

            if (negotiation.getStatus().equals("수락")) {
                item.setStatus("판매 완료");
                itemRepository.save(item);

                negotiation.setStatus(dto.getStatus());
                negotiationRepository.save(negotiation);

                for (Negotiation nego : negotiationRepository.findAllByItemId(itemId)) {
                    if (!nego.getStatus().equals("확정")) {
                        nego.setStatus("거절");
                        negotiationRepository.save(nego);
                    }
                }

                responseDto = new ResponseDto("구매가 확정되었습니다.");
                return responseDto;

            } else {
                throw new NotMatchStatusException();
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseDto updateProposalPrice(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(proposalId);

        if (optionalNegotiation.isEmpty()) {
            throw new NotFoundNegotiationException();
        }

        Negotiation negotiation = optionalNegotiation.get();

        if (!itemId.equals(negotiation.getItemId())) {
            throw new NotFoundItemException();
        }

        if (!dto.getWriter().equals(negotiation.getWriter())) {
            throw new NotMatchWriterException();
        }

        if (!dto.getPassword().equals(negotiation.getPassword())) {
            throw new NotMatchPasswordException();
        }

        negotiation.setSuggestedPrice(dto.getSuggestedPrice());
        negotiationRepository.save(negotiation);

        return new ResponseDto("제안이 수정되었습니다.");
    }
}
