package com.example.mutsamarket.salesitem;

import com.example.mutsamarket.exceptions.status400.NotMatchUserException;
import com.example.mutsamarket.exceptions.status404.NotFoundItemException;
import com.example.mutsamarket.exceptions.status404.NotFoundUserException;
import com.example.mutsamarket.salesitem.entity.SalesItem;
import com.example.mutsamarket.salesitem.dto.RequestItemDto;
import com.example.mutsamarket.salesitem.dto.RequestUserDto;
import com.example.mutsamarket.salesitem.dto.ResponseItemDto;
import com.example.mutsamarket.salesitem.dto.ResponseItemsDto;
import com.example.mutsamarket.user.UserRepository;
import com.example.mutsamarket.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesItemService {
    private final SalesItemRepository itemRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 물품 등록
    public void addItem(RequestItemDto dto) {
        SalesItem salesItem = SalesItem.getInstance(dto);
        UserEntity user = userRepository.findByUsername(dto.getUsername()).orElseThrow(NotFoundUserException::new);

        salesItem.setUser(user);
        itemRepository.save(salesItem);
    }

    // 물품 전체 조회 (페이지 단위)
    public Page<ResponseItemsDto> readAllItems(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        Page<SalesItem> itemEntityPage = itemRepository.findAll(pageable);

        return itemEntityPage.map(ResponseItemsDto::fromEntity);
    }

    // 물품 단일 조회
    public ResponseItemDto readOneItem(Long itemId) {
        SalesItem salesItem = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        return ResponseItemDto.fromEntity(salesItem);
    }

    // 등록 물품 정보 수정
    public void updateItemInfo(Long itemId, RequestItemDto dto) {
        SalesItem salesItem = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);

        checkAuthority(salesItem, dto.getUsername(), dto.getPassword());
        salesItem.updateInfo(dto);
        itemRepository.save(salesItem);
    }

    // 등록 물품 이미지 첨부
    public void updateItemImage(Long itemId, MultipartFile itemImage, String username, String password) {
        SalesItem salesItem = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);
        checkAuthority(salesItem, username, password);

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
        String itemFilename = "itemImage_" + itemId + "." + extension;
        String itemImagePath = itemImagesDir + itemFilename;

        try {
            itemImage.transferTo(Path.of(itemImagePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        salesItem.setImageUrl(String.format("/static/%d/%s", itemId, itemFilename));
        itemRepository.save(salesItem);
    }

    // 등록 물품 삭제
    public void deleteItem(Long itemId, RequestUserDto dto) {
        SalesItem salesItem = itemRepository.findById(itemId).orElseThrow(NotFoundItemException::new);

        checkAuthority(salesItem, dto.getUsername(), dto.getPassword());
        salesItem.getUser().deleteSalesItem(salesItem);

        itemRepository.deleteById(itemId);
    }

    // 사용자 인증 메소드
    private void checkAuthority(SalesItem item, String username, String password) {
        if (!item.getUser().getUsername().equals(username) || !passwordEncoder.matches(password, item.getUser().getPassword())) {
            throw new NotMatchUserException();
        }
    }
}
