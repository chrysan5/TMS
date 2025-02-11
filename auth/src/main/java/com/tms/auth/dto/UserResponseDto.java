package com.tms.auth.dto;

import com.tms.auth.model.DeliveryUser;
import com.tms.auth.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String username;
    private String role;
    private String deliveryType;
    private Long hubId;

    public UserResponseDto(User user){
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.role = String.valueOf(user.getRole());
        this.deliveryType = (user.getDeliveryUser() != null) ? String.valueOf(user.getDeliveryUser().getDeliveryType()) : null;
        this.hubId = (user.getDeliveryUser() != null && user.getDeliveryUser().getHubId() != null) ? user.getDeliveryUser().getHubId() : null;
    }

    public UserResponseDto(DeliveryUser deliveryUser){
        this.userId = deliveryUser.getDeliveryUserId();
        this.username = deliveryUser.getUsername();
        this.role = String.valueOf(deliveryUser.getUser().getRole());
        this.deliveryType = String.valueOf(deliveryUser.getDeliveryType());
        this.hubId = (deliveryUser.getHubId() != null) ? deliveryUser.getHubId() : null;
    }
}
