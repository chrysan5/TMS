package com.tms.auth.model;

import com.tms.auth.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_users")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role = UserRoleEnum.USER;

    private boolean isDelete = false;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void updateUser(SignupRequestDto signupRequestDto){
        this.username = signupRequestDto.getUsername();
        this.password = signupRequestDto.getPassword();
    }
}
