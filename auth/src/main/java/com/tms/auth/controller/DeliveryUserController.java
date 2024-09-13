package com.tms.auth.controller;

import com.tms.auth.dto.DeliveryUserResponseDto;
import com.tms.auth.security.UserDetailsImpl;
import com.tms.auth.service.DeliveryUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("delivery-users")
@Controller
@AllArgsConstructor
public class DeliveryUserController {
    private final DeliveryUserService deliveryUserService;

    //delivery_user 생성은 UserController에서 권한 변경시 진행됨

    //delivery_user 수정, 삭제는 user과 동시에 진행됨

    //자신의 정보만 확인 가능
    @GetMapping
    public ResponseEntity<DeliveryUserResponseDto> getDeliveryUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(deliveryUserService.getDeliveryUser(userDetails.getUser().getUserId()));
    }


}
