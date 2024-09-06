package com.tms.auth.controller;

import com.tms.auth.dto.SignupRequestDto;
import com.tms.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("users")
@Controller
@AllArgsConstructor
public class UserController {

    private final UserService authService;

    @PostMapping("/signup")
    public void signup(@Valid @RequestBody SignupRequestDto userRequestDto) {
        authService.signup(userRequestDto);
    }
}
