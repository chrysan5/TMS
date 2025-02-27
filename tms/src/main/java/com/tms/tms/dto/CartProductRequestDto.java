package com.tms.tms.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@NoArgsConstructor
@Getter
public class CartProductRequestDto {
    @NotNull
    private Long productId;
    @NotNull
    private int quantity;
}
