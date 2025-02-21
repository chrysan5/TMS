package com.tms.tms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDto {
  @NotBlank(message = "상품명을 입력해주세요.")
  private String productName;
  @NotNull
  private BigDecimal price;
  private Long storeId;
}
