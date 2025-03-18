package com.tms.tms.dto;

import com.tms.tms.model.Cart;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;
/*

@Getter
@NoArgsConstructor
public class CartResponseDto {
    private Long cartId;
    private String username;
    private List<CartProductResponseDto> cartProdutList;

    public CartResponseDto(Cart cart){
        this.cartId = cart.getCartId();
        this.username = cart.getUsername();
        this.cartProdutList = cart.getCartProducts().stream().map(CartProductResponseDto::new).collect(Collectors.toList());
    }
}
*/
