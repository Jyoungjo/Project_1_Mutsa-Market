package com.example.mutsamarket.controller;

import com.example.mutsamarket.dto.ResponseDto;
import com.example.mutsamarket.dto.nego.RequestNegotiationDto;
import com.example.mutsamarket.dto.nego.RequestNegotiationUserDto;
import com.example.mutsamarket.dto.nego.ResponseNegotiationDto;
import com.example.mutsamarket.service.NegotiationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/proposal")
public class NegotiationController {
    private final NegotiationService negotiationService;

    // 제안 등록
    @PostMapping
    public ResponseDto createProposal(
            @Valid @RequestBody RequestNegotiationDto dto,
            @PathVariable Long itemId
    ) {
        return negotiationService.addProposal(itemId, dto);
    }

    // 제안 조회
    @GetMapping
    public Page<ResponseNegotiationDto> readProposal(
            @PathVariable Long itemId,
            @RequestParam("writer") String writer,
            @RequestParam("password") String password,
            @RequestParam("page") Integer page
    ) {
        return negotiationService.readProposal(itemId, writer, password, page);
    }

    // 제안 수정
    @PutMapping("/{proposalId}")
    public ResponseDto updateProposal(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @Valid @RequestBody RequestNegotiationDto dto
    ) {
        return negotiationService.updateProposal(itemId, proposalId, dto);
    }

    // 제안 삭제
    @DeleteMapping("/{proposalId}")
    public ResponseDto deleteProposal(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @RequestBody RequestNegotiationUserDto dto
    ) {
        return negotiationService.deleteProposal(itemId, proposalId, dto);
    }
}
