package com.tms.tms.controller;

import com.tms.tms.dto.OrderRequestDto;
import com.tms.tms.dto.OrderResponseDto;
import com.tms.tms.dto.StoreResponseDto;
import com.tms.tms.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("orders")
@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    //모든 권한 가능
    @PostMapping("/store/{storeId}")
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderRequestDto requestDto,
            @PathVariable("storeId") Long storeId
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(requestDto, storeId));
    }

    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB', 'STORE')")
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(
            @Valid @RequestBody OrderRequestDto requestDto,
            @PathVariable("orderId") Long orderId,
            @RequestHeader(value = "X-username", required = true) String username,
            @RequestHeader(value = "X-role", required = true) String role
    ){
        return ResponseEntity.ok(orderService.updateOrder(requestDto, orderId, username, role));
    }

    //주문 취소
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB', 'STORE')")
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable("orderId") Long orderId,
            @RequestHeader(value = "X-username", required = true) String username,
            @RequestHeader(value = "X-role", required = true) String role
    ){
        orderService.cancelOrder(orderId, username, role);
        return ResponseEntity.noContent().build();
    }


    //본인의 주문만 검색 가능
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(
            @PathVariable("orderId") Long orderId,
            @RequestHeader(value = "X-username", required = true) String username
    ){
        return ResponseEntity.ok(orderService.getOrder(orderId, username));
    }
    
    //가게별 주문 보기
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByStore(@PathVariable("storeId") Long storeId){
        return ResponseEntity.ok(orderService.getOrdersByStore(storeId));
    }

    //orderLocation 변경
    //@PreAuthorize("hasAnyAuthority('MASTER', 'HUB')")
    @PutMapping("/{orderId}/location")
    public ResponseEntity<OrderResponseDto> updateOrderLocation(
            @RequestParam("location") String location,
            @PathVariable("orderId") Long orderId
    ){
        return ResponseEntity.ok(orderService.updateOrderLocation(location, orderId));
    }

    //orderState 변경
    //@PreAuthorize("hasAnyAuthority('MASTER', 'HUB')")
    @PutMapping("/{orderId}/state")
    public ResponseEntity<OrderResponseDto> updateOrderState(
            @RequestParam("state") String state,
            @PathVariable("orderId") Long orderId
    ){
        return ResponseEntity.ok(orderService.updateOrderState(state, orderId));
    }
}
