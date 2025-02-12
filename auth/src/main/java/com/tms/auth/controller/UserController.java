package com.tms.auth.controller;

import com.tms.auth.dto.RoleUpdateRequestDto;
import com.tms.auth.dto.UserRequestDto;
import com.tms.auth.dto.UserResponseDto;
import com.tms.auth.security.UserDetailsImpl;
import com.tms.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("users")
@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    //회원 가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(userRequestDto));
    }

    //회원 정보 수정
    @PreAuthorize("hasAuthority('ROLE_MASTER') or isAuthenticated()")
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(
            @Valid @RequestBody UserRequestDto requestDto,
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(userService.updateUser(requestDto, userId));
    }

    //기본 가입시 USER 권한으로 가입 후 권한 변경시킴
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    @PutMapping("/{userId}/role")
    public ResponseEntity<UserResponseDto> updateUserRole(
            @Valid @RequestBody RoleUpdateRequestDto requestDto,
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(userService.updateUserRole(requestDto, userId));
    }

    //유저 삭제 및 회원 탈퇴
    @PreAuthorize("hasAuthority('ROLE_MASTER') or isAuthenticated()")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    //본인 정보 조회
    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(userService.getUser(userDetails.getUser().getUserId()));
    }

    //전체 유저 조회 (삭제된 유저 포함)
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDto>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    //전체 유저 조회 및 검색
    //예시) localhost:19091/users/search?page=0&size=10&sort=username,asc&isDelete=false
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDto>> searchUsers(UserRequestDto userRequestDto, Pageable pageable){
        return ResponseEntity.ok(userService.searchUsers(userRequestDto, pageable));
    }


    //유저 확인
    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyUser(@RequestParam(value = "username") String username) {
        return ResponseEntity.ok(userService.verifyUser(username));
    }
}
