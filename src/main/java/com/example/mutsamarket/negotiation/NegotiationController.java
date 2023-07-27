package com.example.mutsamarket.negotiation;

import com.example.mutsamarket.response.ResponseDto;
import com.example.mutsamarket.negotiation.entity.NegotiationStatus;
import com.example.mutsamarket.negotiation.dto.RequestNegotiationDto;
import com.example.mutsamarket.negotiation.dto.RequestNegotiationUserDto;
import com.example.mutsamarket.negotiation.dto.ResponseNegotiationDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
            @PathVariable Long itemId
    ) {
        negotiationService.addProposal(itemId, dto);
        return ResponseEntity.ok(ResponseDto.getInstance("구매 제안이 등록되었습니다."));
    }

    // 제안 조회
    @GetMapping
    public ResponseEntity<Page<ResponseNegotiationDto>> readProposal(
            @PathVariable Long itemId,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("page") Integer page
    ) {
        return ResponseEntity.ok(negotiationService.readProposal(itemId, username, password, page));
    }

    // 제안 수정
    @PutMapping("/{proposalId}")
    public ResponseEntity<ResponseDto> updateProposal(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @Valid @RequestBody RequestNegotiationDto dto
    ) {
        // 가격 변경
        if (dto.getStatus() == null) {
            negotiationService.updatePrice(itemId, proposalId, dto);
            return ResponseEntity.ok(ResponseDto.getInstance("제안이 수정되었습니다."));
        }

        else {
            if (dto.getStatus().equals(NegotiationStatus.CONFIRMED.getStatus())) {
                negotiationService.acceptProposal(itemId, proposalId, dto);
                return ResponseEntity.ok(ResponseDto.getInstance("구매가 확정되었습니다."));
            } else {
                negotiationService.updateStatus(itemId, proposalId, dto);
                return ResponseEntity.ok(ResponseDto.getInstance("제안의 상태가 변경되었습니다."));
            }
        }
    }

    // 제안 삭제
    @DeleteMapping("/{proposalId}")
    public ResponseEntity<ResponseDto> deleteProposal(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @RequestBody RequestNegotiationUserDto dto
    ) {
        negotiationService.deleteProposal(itemId, proposalId, dto);
        return ResponseEntity.ok(ResponseDto.getInstance("제안을 삭제했습니다."));
    }
}
