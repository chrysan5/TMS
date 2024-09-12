package com.tms.auth.dto;

import com.tms.auth.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String username;
    private String password;
    private String role;

    public UserResponseDto(User user){
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = String.valueOf(user.getRole());
    }
}
