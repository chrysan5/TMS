package com.tms.tms.dto;

import com.tms.tms.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ProductResponseDto {
  private Long productId;
  private String productName;
  private Integer productPrice;
  private Long storeId;
  private Long hubId;

  public ProductResponseDto(Product product){
    this.productId = product.getProductId();
    this.productName = product.getProductName();
    this.productPrice = product.getProductPrice();
    this.storeId = product.getStore().getStoreId();
    this.hubId = product.getHubId();
  }
}
