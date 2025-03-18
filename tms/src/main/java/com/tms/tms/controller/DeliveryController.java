package com.tms.tms.controller;

import com.tms.tms.dto.DeliveryRequestDto;
import com.tms.tms.dto.DeliveryResponseDto;
import com.tms.tms.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("delivery")
@RequiredArgsConstructor
@RestController
public class DeliveryController {
    private final DeliveryService deliveryService;

    //배송 생성, 수정, 취소는 order와 같이 진행된다.

    //배송 조회 단일
    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponseDto> getDelivery(@PathVariable("deliveryId") Long deliveryId){
        return ResponseEntity.ok(deliveryService.getDelivery(deliveryId));
    }


    //배송 조회 복수(리스트) : 허브별 배송 조회
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB')")
    @GetMapping("/list/hub/{hubId}")
    public ResponseEntity<List<DeliveryResponseDto>> getDeliveriesByHub(@PathVariable("hubId") Long hubId){
        return ResponseEntity.ok(deliveryService.getDeliveriesByHub(hubId));
    }

    //store별 배송 조회
    @GetMapping("/list/store/{storeId}")
    public ResponseEntity<List<DeliveryResponseDto>> getDeliveriesByStore(@PathVariable("storeId") Long storeId){
        return ResponseEntity.ok(deliveryService.getDeliveriesByStore(storeId));
    }
    

    //배송 위치 변경
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB')")
    @PutMapping("/{deliveryId}/location")
    public ResponseEntity<DeliveryResponseDto> updateOrderLocation(
            @PathVariable("deliveryId") Long deliveryId,
            @Valid @RequestBody DeliveryRequestDto deliveryRequestDto
    ){
        return ResponseEntity.ok(deliveryService.updateOrderLocation(deliveryId, deliveryRequestDto));
    }

}
