package com.tms.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String role;
}
