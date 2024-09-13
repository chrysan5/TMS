package com.tms.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RoleUpdateRequestDto {
    @NotBlank(message = "role을 입력해 주세요.")
    private String role;
    private Long hubId;
}
