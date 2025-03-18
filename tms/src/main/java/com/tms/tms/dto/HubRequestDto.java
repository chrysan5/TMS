package com.tms.tms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
public class HubRequestDto {
    @NotBlank(message = "허브 지역을 입력해주세요.")
    private String hubRegion;
    @NotBlank(message = "위도를 입력해주세요.")
    private String hubAddressX;
    @NotBlank(message = "경도를 입력해주세요.")
    private String hubAddressY;
}
