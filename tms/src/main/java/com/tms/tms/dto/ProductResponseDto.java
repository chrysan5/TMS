package com.tms.tms.dto;

import com.tms.tms.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Getter
@NoArgsConstructor
public class ProductResponseDto {
  private Long productId;
  private String productName;
  private BigDecimal price;
  private Long storeId;
  private Long hubId;

  public ProductResponseDto(Product product){
    this.productId = product.getProductId();
    this.productName = product.getProductName();
    this.price = product.getPrice();
    this.storeId = product.getStore().getStoreId();
    this.hubId = product.getHubId();
  }
}
