package com.tms.tms.dto;

import com.tms.tms.model.Delivery;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryResponseDto {
    private Long deliveryId;
    private String address;
    private String location;
    private Long startHubId;
    private Long endHubId;
    private boolean isDelete;
    private Long orderId;

    public DeliveryResponseDto(Delivery delivery){
        this.deliveryId = delivery.getDeliveryId();
        this.address = delivery.getAddress();
        this.location = String.valueOf(delivery.getLocation());
        this.startHubId = delivery.getStartHubId();
        this.endHubId = delivery.getEndHubId();
        this.isDelete = delivery.isDelete();
        this.orderId = delivery.getOrder().getOrderId();
    }
}
