package com.tms.tms.controller;

import com.tms.tms.dto.ProductResponseDto;
import com.tms.tms.dto.StoreRequestDto;
import com.tms.tms.dto.StoreResponseDto;
import com.tms.tms.service.ProductService;
import com.tms.tms.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;


@RequestMapping("/stores")
@RequiredArgsConstructor
@RestController
public class StoreController {
    private final StoreService storeService;
    private final ProductService productService;

    @PreAuthorize("hasAnyAuthority('MASTER', 'STORE')")
    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(
            @Valid @RequestBody StoreRequestDto storeRequestDto,
            Principal principal
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.createStore(storeRequestDto, principal.getName()));
    }

    @PreAuthorize("hasAnyAuthority('MASTER', 'STORE')")
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(
            @PathVariable("storeId") Long storeId,
            @Valid @RequestBody StoreRequestDto StoreRequestDto,
            @RequestHeader(value = "X-username", required = true) String username,
            @RequestHeader(value = "X-role", required = true) String role
    ){
        return ResponseEntity.ok(storeService.updateStore(storeId, StoreRequestDto, username, role));
    }

    @PreAuthorize("hasAnyAuthority('MASTER', 'STORE')")
    @DeleteMapping("/{storeId}")
    public ResponseEntity deleteStore(
            @PathVariable("storeId") Long storeId,
            @RequestHeader(value = "X-username", required = true) String username,
            @RequestHeader(value = "X-role", required = true) String role
    ){
        storeService.deleteStore(storeId, username, role);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getStore(@PathVariable("storeId") Long storeId){
        return ResponseEntity.ok(storeService.getStore(storeId));
    }

    //가게 전체 조회
    @GetMapping
    public ResponseEntity<Page<StoreResponseDto>> getStores(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc
    ) {
        return ResponseEntity.ok(storeService.getStores(page, size, sortBy, isAsc));
    }

    //가게별 상품 조회
    @GetMapping("/{storeId}/products")
    public ResponseEntity<List<ProductResponseDto>> getProductByStoreId(
            @PathVariable("storeId") Long storeId){
        return ResponseEntity.ok(productService.getProductByStore(storeId));
    }

    //가게 검색
    @GetMapping("/search")
    public ResponseEntity<List<StoreResponseDto>> getStoreSearch(
            @RequestParam("keyword") String keyword){
        return ResponseEntity.ok(storeService.getStoreSearch(keyword));
    }

    //가게별 상품 검색
    @GetMapping("/{storeId}/product/search")
    public ResponseEntity<List<ProductResponseDto>> getProductSearchByStore(
            @PathVariable("storeId") Long storeId,
            @RequestParam("keyword") String keyword
    ){
        return ResponseEntity.ok(productService.getProductSearchByStore(storeId, keyword));
    }

}
