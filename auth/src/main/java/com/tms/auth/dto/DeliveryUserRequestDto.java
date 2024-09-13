package com.tms.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DeliveryUserRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotNull
    private Long hubId;
}
