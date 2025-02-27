package com.tms.tms.controller;

import com.tms.tms.dto.CartProductListDto;
import com.tms.tms.dto.CartProductRequestDto;
import com.tms.tms.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("carts")
@RequiredArgsConstructor
@RestController
public class CartController {
    private final CartService cartService;
    
    //장바구니에 상품 추가
    @PostMapping("/cart-product")
    public ResponseEntity<String> putProductInCart(
            @Valid @RequestBody CartProductRequestDto cartProductRequestDto,
            Principal principal
    ){
        cartService.putProductInCart(cartProductRequestDto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body("상품을 장바구니에 추가하였습니다.");
    }

    //장바구니에 상품 갯수 수정
    @PutMapping("/cart-product")
    public ResponseEntity<String> updateProductInCart(
            @Valid @RequestBody CartProductRequestDto cartProductRequestDto,
            Principal principal
    ){
        cartService.updateProductInCart(cartProductRequestDto, principal.getName());
        return ResponseEntity.ok("상품 수량이 수정되었습니다.");
    }
    
    //장바구니의 상품 삭제
    @DeleteMapping("/cart-product")
    public ResponseEntity removeProductInCart(@RequestBody List<Long> productIds, Principal principal) {
        cartService.removeProductInCart(productIds, principal.getName());
        return ResponseEntity.noContent().build();
    }

    //장바구니 삭제 - 장바구니의 상품을 모두 삭제 후 장바구니 삭제
    @DeleteMapping
    public ResponseEntity removeCart(Principal principal) {
        cartService.removeCart( principal.getName());
        return ResponseEntity.noContent().build();
    }

    //장바구니의 상품 조회
    @GetMapping
    public ResponseEntity<Page<CartProductListDto>> getCart(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc,
            Principal principal

    ) {
        return ResponseEntity.ok(cartService.getCart(page, size, isAsc, principal.getName()));
    }

}
