package com.tms.tms.dto;

import com.tms.tms.model.Hub;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HubResponseDto {
    private Long hubId;
    private String hubRegion;
    private String hubAddressX;
    private String hubAddressY;

    public HubResponseDto(Hub hub){
        this.hubId = hub.getHubId();
        this.hubRegion = hub.getHubRegion();
        this.hubAddressX = hub.getHubAddressX();
        this.hubAddressY = hub.getHubAddressY();
    }
}
