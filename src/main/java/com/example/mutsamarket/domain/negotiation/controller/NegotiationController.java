package com.example.mutsamarket.domain.negotiation.controller;

import com.example.mutsamarket.domain.negotiation.service.NegotiationService;
import com.example.mutsamarket.domain.negotiation.dto.RequestNegotiationDto;
import com.example.mutsamarket.domain.negotiation.dto.ResponseNegotiationDto;
import com.example.mutsamarket.global.response.ResponseDto;
import com.example.mutsamarket.domain.negotiation.domain.NegotiationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/proposal")
public class NegotiationController {
    private final NegotiationService negotiationService;

    // 제안 등록
    @PostMapping
    public ResponseEntity<ResponseDto> createProposal(
            @Valid @RequestBody RequestNegotiationDto dto,
            @PathVariable Long itemId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        negotiationService.addProposal(itemId, dto, username);
        return ResponseEntity.ok(ResponseDto.getInstance("구매 제안이 등록되었습니다."));
    }

    // 제안 조회
    @GetMapping
    public ResponseEntity<Page<ResponseNegotiationDto>> readProposal(
            @PathVariable Long itemId,
            @RequestParam("username") String username,
            @RequestParam(value = "page", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "25") Integer pageSize
    ) {
        return ResponseEntity.ok(negotiationService.readProposal(itemId, username, pageNum, pageSize));
    }

    // 제안 수정
    @PutMapping("/{proposalId}")
    public ResponseEntity<ResponseDto> updateProposal(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @Valid @RequestBody RequestNegotiationDto dto,
            Authentication authentication
    ) {
        String username = authentication.getName();
        // 가격 변경
        if (dto.getStatus() == null) {
            negotiationService.updatePrice(itemId, proposalId, dto, username);
            return ResponseEntity.ok(ResponseDto.getInstance("제안이 수정되었습니다."));
        }

        else {
            if (dto.getStatus().equals(NegotiationStatus.CONFIRMED.getStatus())) {
                negotiationService.acceptProposal(itemId, proposalId, dto, username);
                return ResponseEntity.ok(ResponseDto.getInstance("구매가 확정되었습니다."));
            } else {
                negotiationService.updateStatus(itemId, proposalId, dto, username);
                return ResponseEntity.ok(ResponseDto.getInstance("제안의 상태가 변경되었습니다."));
            }
        }
    }

    // 제안 삭제
    @DeleteMapping("/{proposalId}")
    public ResponseEntity<ResponseDto> deleteProposal(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        negotiationService.deleteProposal(itemId, proposalId, username);
        return ResponseEntity.ok(ResponseDto.getInstance("제안을 삭제했습니다."));
    }
}
