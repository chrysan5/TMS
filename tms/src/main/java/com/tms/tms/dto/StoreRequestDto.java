package com.tms.tms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@NoArgsConstructor
@Getter
public class StoreRequestDto {
    @NotBlank(message = "상호명을 입력해주세요.")
    private String storeName;
    private String storeAddress;
    @NotNull
    private Long hubId;
}
