package com.tms.tms.dto;

import com.tms.tms.model.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor
public class StoreResponseDto {
    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String username;
    private Long hubId;
    private List<ProductResponseDto> productList;

    public StoreResponseDto(Store store){
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.storeAddress = store.getStoreAddress();
        this.username = store.getUsername();
        this.hubId = store.getHub().getHubId();
        this.productList = store.getProductList().stream().map(ProductResponseDto::new).collect(Collectors.toList());
    }
}
