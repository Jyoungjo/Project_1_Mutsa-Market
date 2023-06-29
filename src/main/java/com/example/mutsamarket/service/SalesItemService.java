package com.example.mutsamarket.service;

import com.example.mutsamarket.Entity.SalesItem;
import com.example.mutsamarket.dto.SalesItemDto;
import com.example.mutsamarket.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesItemService {
    private final SalesItemRepository itemRepository;

    // 물품 등록
    public SalesItemDto addItem(SalesItemDto dto) {
        SalesItem newItem = new SalesItem();
        newItem.setTitle(dto.getTitle());
        newItem.setDescription(dto.getDescription());
        newItem.setMinPriceWanted(dto.getMinPriceWanted());
        newItem.setStatus("판매중");
        newItem.setWriter(dto.getWriter());
        newItem.setPassword(dto.getPassword());

        return SalesItemDto.fromEntity(itemRepository.save(newItem));
    }

    // 물품 전체 조회 (페이지 단위)
    public Page<SalesItemDto> readAllItems(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("itemId"));
        Page<SalesItem> itemEntityPage = itemRepository.findAll(pageable);

        return itemEntityPage.map(SalesItemDto::fromEntity);
    }

    // 물품 단일 조회
    public SalesItemDto readOneItem(Long itemId) {
        Optional<SalesItem> optionalSalesItem = itemRepository.findById(itemId);
        if (optionalSalesItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return SalesItemDto.fromEntity(optionalSalesItem.get());
    }

    // 등록 물품 정보 수정
    public SalesItemDto updateItemInfo(Long itemId, SalesItemDto dto) {
        Optional<SalesItem> optionalSalesItem = itemRepository.findById(itemId);
        if (optionalSalesItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        SalesItem salesItem = optionalSalesItem.get();

        if (dto.getPassword().equals(salesItem.getPassword())) {
            salesItem.setTitle(dto.getTitle());
            salesItem.setDescription(dto.getDescription());
            salesItem.setMinPriceWanted(dto.getMinPriceWanted());
            salesItem.setWriter(dto.getWriter());
            salesItem.setPassword(dto.getPassword());
            return SalesItemDto.fromEntity(itemRepository.save(salesItem));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    // 등록 물품 이미지 첨부
    public SalesItemDto updateItemImage(Long itemId, MultipartFile itemImage) {
        Optional<SalesItem> optionalSalesItem = itemRepository.findById(itemId);
        if (optionalSalesItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        String itemImagesDir = String.format("itemImages/%d", itemId);
        try {
            Files.createDirectories(Path.of(itemImagesDir));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String originalFilename = itemImage.getOriginalFilename();
        String[] fileNameSplit = originalFilename.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        String itemFilename = "itemImage." + extension;
        String itemImagePath = itemImagesDir + itemFilename;

        try {
            itemImage.transferTo(Path.of(itemImagePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        SalesItem salesItem = optionalSalesItem.get();
        salesItem.setImageUrl(String.format("/static/%d/%s", itemId, itemFilename));
        return SalesItemDto.fromEntity(itemRepository.save(salesItem));
    }

    // 등록 물품 삭제
    public void deleteItem(Long itemId, SalesItemDto dto) {
        Optional<SalesItem> optionalSalesItem = itemRepository.findById(itemId);
        if (optionalSalesItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        SalesItem salesItem = optionalSalesItem.get();
        if (dto.getPassword().equals(salesItem.getPassword())) {
            itemRepository.deleteById(itemId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        }
    }
}
