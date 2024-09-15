package com.tms.tms.dto;


import com.tms.tms.model.Order;
import com.tms.tms.model.Product;
import com.tms.tms.model.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private String state;
    private Long receiveStoreId;
    private String location;
    private Long startHubId;
    private Long endHubId;
    private StoreResponseDto storeResponseDto;
    private ProductResponseDto productResponseDto;
    private Integer productQuantity;

    public OrderResponseDto(Order order){
        this.orderId = order.getOrderId();
        this.state = String.valueOf(order.getState());
        this.receiveStoreId = order.getReceiveStoreId();
        this.location = String.valueOf(order.getLocation());
        this.startHubId = order.getStartHubId();
        this.endHubId = order.getEndHubId();
        this.storeResponseDto = new StoreResponseDto(order.getStore());
        this.productResponseDto = new ProductResponseDto(order.getProduct());
        this.productQuantity = order.getProductQuantity();
    }
}
