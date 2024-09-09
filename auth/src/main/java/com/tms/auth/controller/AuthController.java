package com.tms.auth.controller;

import com.tms.auth.dto.SignupRequestDto;
import com.tms.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("auth")
@Controller
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequestDto userRequestDto) {
        authService.signup(userRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyUser(@RequestParam(value = "username") String username) {
        return ResponseEntity.ok(authService.verifyUser(username));
    }

}
