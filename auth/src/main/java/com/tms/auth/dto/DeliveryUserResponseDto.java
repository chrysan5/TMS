package com.tms.auth.dto;

import com.tms.auth.model.DeliveryUser;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryUserResponseDto {
    private Long deliveryUserId;
    private String username;
    private String password;
    private Long hubId;

    public DeliveryUserResponseDto(DeliveryUser deliveryUser){
        this.deliveryUserId = deliveryUser.getDeliveryUserId();
        this.username = deliveryUser.getUsername();
        this.password = deliveryUser.getPassword();
        this.hubId = deliveryUser.getHubId();
    }
}
