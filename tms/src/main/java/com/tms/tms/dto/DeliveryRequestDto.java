package com.tms.tms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
public class DeliveryRequestDto {
    @NotBlank(message = "location을 입력해주세요.")
    private String location;
}
