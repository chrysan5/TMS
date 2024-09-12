package com.tms.tms.controller;

import com.tms.tms.dto.ProductRequestDto;
import com.tms.tms.dto.ProductResponseDto;
import com.tms.tms.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/products")
@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasAnyAuthority('MASTER', 'STORE')")
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequestDto));
    }

    @PreAuthorize("hasAnyAuthority('MASTER', 'STORE')")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable("productId") Long productId,
            @Valid @RequestBody ProductRequestDto productRequestDto,
            @RequestHeader(value = "X-username", required = true) String username,
            @RequestHeader(value = "X-role", required = true) String role
    ){
        return ResponseEntity.ok(productService.updateProduct(productId, productRequestDto, username, role));
    }

    @PreAuthorize("hasAnyAuthority('MASTER', 'STORE')")
    @DeleteMapping("/{productId}")
    public ResponseEntity deleteProduct(
            @PathVariable("productId") Long productId,
            @RequestHeader(value = "X-username", required = true) String username,
            @RequestHeader(value = "X-role", required = true) String role
    ){
        productService.deleteProduct(productId, username, role);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("productId") Long productId){
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    //상품 전체 조회
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc
    ) {
        return ResponseEntity.ok(productService.getProducts(page, size, sortBy, isAsc));
    }


}
