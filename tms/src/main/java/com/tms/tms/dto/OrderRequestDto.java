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
    private Long receiveStoreId;
    @NotNull
    private Long productId;
    @NotNull
    private Integer productQuantity;
}
