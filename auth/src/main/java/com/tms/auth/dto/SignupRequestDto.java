package com.tms.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    private Enum role;
}
