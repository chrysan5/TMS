package com.tms.tms.dto;


import com.tms.tms.model.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private String state;
    private String username;
    private Long requestStoreId; //주문하는 업체
    private Long sellerStoreId; //주문받는 업체
    private int totalQuantity;
    private String totalPrice;
    private Long deliveryId;
    private List<OrderProductResponseDto> orderProductResponseDtoList;


    public OrderResponseDto(Order order){
        this.orderId = order.getOrderId();
        this.state = String.valueOf(order.getState());
        this.username = order.getUsername();
        this.requestStoreId = order.getStore().getStoreId();
        this.sellerStoreId = order.getSellerStoreId();
        this.totalQuantity = order.getTotalQuantity();
        this.totalPrice = String.valueOf(order.getTotalPrice());
        //this.deliveryId = order.getProductQuantity();
        this.orderProductResponseDtoList = order.getOrderProducts().stream().map(OrderProductResponseDto::new).collect(Collectors.toList());
    }
}
