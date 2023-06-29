package com.example.mutsamarket.service;

import com.example.mutsamarket.Entity.SalesItem;
import com.example.mutsamarket.dto.item.*;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesItemService {
    private final SalesItemRepository itemRepository;

    // 물품 등록
    public ResponseDto addItem(ItemDto dto) {
        SalesItem newItem = new SalesItem();
        newItem.setTitle(dto.getTitle());
        newItem.setDescription(dto.getDescription());
        newItem.setMinPriceWanted(dto.getMinPriceWanted());
        newItem.setStatus("판매중");
        newItem.setWriter(dto.getWriter());
        newItem.setPassword(dto.getPassword());
        ItemDto.fromEntity(itemRepository.save(newItem));
        return new ResponseDto("등록이 완료되었습니다.");
    }

    // 물품 전체 조회 (페이지 단위)
    public Page<ResponsePageDto> readAllItems(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        Page<SalesItem> itemEntityPage = itemRepository.findAll(pageable);

        return itemEntityPage.map(ResponsePageDto::fromEntity);
    }

    // 물품 단일 조회
    public ResponseItemDto readOneItem(Long itemId) {
        Optional<SalesItem> optionalSalesItem = itemRepository.findById(itemId);
        if (optionalSalesItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResponseItemDto.fromEntity(optionalSalesItem.get());
    }

    // 등록 물품 정보 수정
    public ResponseDto updateItemInfo(Long itemId, ItemDto dto) {
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
            ItemDto.fromEntity(itemRepository.save(salesItem));
            return new ResponseDto("물품이 수정되었습니다.");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    // 등록 물품 이미지 첨부
    public ResponseDto updateItemImage(Long itemId, MultipartFile itemImage) {
        Optional<SalesItem> optionalSalesItem = itemRepository.findById(itemId);
        if (optionalSalesItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        String itemImagesDir = String.format("itemImages/%d/", itemId);
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
        itemRepository.save(salesItem);
        return new ResponseDto("이미지가 등록되었습니다.");
    }

    // 등록 물품 삭제
    public ResponseDto deleteItem(Long itemId, ItemDto dto) {
        Optional<SalesItem> optionalSalesItem = itemRepository.findById(itemId);
        if (optionalSalesItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        SalesItem salesItem = optionalSalesItem.get();
        if (!dto.getPassword().equals(salesItem.getPassword()) || !dto.getWriter().equals(salesItem.getWriter())) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        }

        itemRepository.deleteById(itemId);

        return new ResponseDto("물품을 삭제했습니다.");
    }
}
