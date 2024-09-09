package com.tms.auth.dto;

import com.tms.auth.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;

    public LoginRequestDto(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
    }
}