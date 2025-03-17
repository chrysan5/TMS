package com.tms.tms.dto;

import com.tms.tms.model.OrderProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderProductResponseDto {
    private Long orderProductId;
    private Long orderId;
    private Long productId;
    private String productName;
    private int quantity;
    private String price;

    public OrderProductResponseDto(OrderProduct orderProduct){
        this.orderProductId = orderProduct.getOrderProductId();
        this.orderId = orderProduct.getOrder().getOrderId();
        this.productId = orderProduct.getProduct().getProductId();
        this.productName = orderProduct.getProduct().getProductName();
        this.quantity = orderProduct.getQuantity();
        this.price = String.valueOf(orderProduct.getPrice());
    }
}
