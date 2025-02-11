package com.tms.auth.controller;

import com.tms.auth.dto.UserRequestDto;
import com.tms.auth.dto.UserResponseDto;
import com.tms.auth.service.DeliveryUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RequestMapping("users")
@Controller
@AllArgsConstructor
public class DeliveryUserController {
    private final DeliveryUserService deliveryUserService;

    //회원 가입 : delivery_user 생성시 user가 같이 생성됨
    @PostMapping("/signup/delivery-user")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryUserService.signup(userRequestDto));
    }

    //delivery_user 수정, 삭제는 user과 동시에 진행됨
    //조회는 user를 통해서 가능
}
