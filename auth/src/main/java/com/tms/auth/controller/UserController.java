package com.tms.auth.controller;

import com.tms.auth.dto.RoleUpdateRequestDto;
import com.tms.auth.dto.UserRequestDto;
import com.tms.auth.dto.UserResponseDto;
import com.tms.auth.security.UserDetailsImpl;
import com.tms.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("users")
@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    //마스터 권한만 유저 생성,수정,삭제 가능
    @PreAuthorize("hasAuthority('MASTER')")
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(userRequestDto));
    }

    @PreAuthorize("hasAuthority('MASTER')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(
            @Valid @RequestBody UserRequestDto requestDto,
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(userService.updateUser(requestDto, userId));
    }

    //기본 가입시 USER 권한으로 가입 -> 이후 권한을 변경시 권한이 HUB일 경우 delivery_user 생성됨
    @PreAuthorize("hasAuthority('MASTER')")
    @PutMapping("/{userId}/role")
    public ResponseEntity<UserResponseDto> updateUserRole(
            @Valid @RequestBody RoleUpdateRequestDto requestDto,
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(userService.updateUserRole(requestDto, userId));
    }

    @PreAuthorize("hasAuthority('MASTER')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(userService.getUser(userDetails.getUser().getUserId()));
    }


    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyUser(@RequestParam(value = "username") String username) {
        return ResponseEntity.ok(userService.verifyUser(username));
    }
}
