package com.example.mutsamarket.salesitem;

import com.example.mutsamarket.ResponseDto;
import com.example.mutsamarket.salesitem.dto.RequestItemDto;
import com.example.mutsamarket.salesitem.dto.RequestUserDto;
import com.example.mutsamarket.salesitem.dto.ResponseItemDto;
import com.example.mutsamarket.salesitem.dto.ResponseItemsDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class SalesItemController {

    private final SalesItemService itemService;

    @PostMapping
    public ResponseEntity<ResponseDto> create(@Valid @RequestBody RequestItemDto dto) {
        itemService.addItem(dto);
        return ResponseEntity.ok(ResponseDto.getInstance("등록이 완료되었습니다."));
    }

    @GetMapping
    public ResponseEntity<Page<ResponseItemsDto>> readAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "25") Integer limit
    ) {
        return ResponseEntity.ok(itemService.readAllItems(page, limit));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ResponseItemDto> readOne(
            @PathVariable("itemId") Long itemId
    ) {
        return ResponseEntity.ok(itemService.readOneItem(itemId));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ResponseDto> update(
            @PathVariable("itemId") Long itemId, @Valid @RequestBody RequestItemDto dto
    ) {
        itemService.updateItemInfo(itemId, dto);
        return ResponseEntity.ok(ResponseDto.getInstance("물품이 수정되었습니다."));
    }

    @PutMapping(value = "/{itemId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> updateImage(
            @PathVariable("itemId") Long itemId,
            @RequestParam("image") MultipartFile itemImage,
            @RequestParam("writer") String writer,
            @RequestParam("password") String password
    ) {
        itemService.updateItemImage(itemId, itemImage, writer, password);
        return ResponseEntity.ok(ResponseDto.getInstance("이미지가 등록되었습니다."));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ResponseDto> delete(
            @PathVariable("itemId") Long itemId, @RequestBody RequestUserDto dto
    ) {
        itemService.deleteItem(itemId, dto);
        return ResponseEntity.ok(ResponseDto.getInstance("물품을 삭제했습니다."));
    }
}
