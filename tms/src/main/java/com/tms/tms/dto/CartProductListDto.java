package com.tms.tms.dto;

import com.tms.tms.model.CartProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartProductListDto {
    private Long productId;
    private String productName;
    private int quantity;

    public CartProductListDto(CartProduct cartProduct){
        this.productId = cartProduct.getProduct().getProductId();
        this.productName = cartProduct.getProduct().getProductName();
        this.quantity = cartProduct.getQuantity();
    }
}
