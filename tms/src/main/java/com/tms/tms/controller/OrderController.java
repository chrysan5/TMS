package com.tms.tms.controller;

import com.tms.tms.dto.OrderRequestDto;
import com.tms.tms.dto.OrderResponseDto;
import com.tms.tms.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("orders")
@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    //모든 권한 가능
    //주문 생성 - 장바구니 상품 전체 주문, 배송 생성 동시에
    //@PostMapping("/store/{storeId}")
    @PostMapping
    public ResponseEntity<Void> createOrder(
        @Valid @RequestBody OrderRequestDto orderRequestDto,
        Principal principal
    ){
        orderService.createOrder(orderRequestDto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //주문 수정
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto,
            @PathVariable("orderId") Long orderId
    ){
        return ResponseEntity.ok(orderService.updateOrder(orderRequestDto, orderId));
    }


    //주문 조회 단일 : 본인의 주문만 조회 가능
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(
            @PathVariable("orderId") Long orderId,
            @RequestHeader(value = "X-username", required = true) String username
    ){
        return ResponseEntity.ok(orderService.getOrder(orderId, username));
    }
    
    //주문 조회 복수(리스트) : 가게별 주문 조회
    @GetMapping("/store")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByStore(Principal principal){
        return ResponseEntity.ok(orderService.getOrdersByStore(principal.getName()));
    }

    
    //주문 취소는 관리자, 유저가 가능하고 완료는 배송이 완료되면 되도록 설정
    //주문 취소
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(
            @PathVariable("orderId") Long orderId,
            @RequestHeader(value = "X-role", required = true) String role
    ){
        OrderResponseDto orderResponseDto = orderService.cancelOrder(orderId, role);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order canceled successfully");
        response.put("orderId", orderId);
        response.put("state", orderResponseDto.getState());

        return ResponseEntity.ok(response);
    }
}
