package com.tms.tms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDto {
    @NotNull
    private Long sellerStoreId; //주문받는 업체
    @NotBlank(message = "주소를 입력해주세요.")
    private String address;
}

