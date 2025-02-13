package com.tms.tms.controller;

import com.tms.tms.dto.HubRequestDto;
import com.tms.tms.dto.HubResponseDto;
import com.tms.tms.service.HubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RequestMapping("hubs")
@RequiredArgsConstructor
@RestController
public class HubController {
    private final HubService hubService;

    //허브 생성
    @PreAuthorize("hasAuthority('MASTER')")
    @PostMapping
    public ResponseEntity<HubResponseDto> createHub(@Valid @RequestBody HubRequestDto hubRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(hubService.createHub(hubRequestDto));
    }

    //허브 수정
    @PreAuthorize("hasAuthority('MASTER')")
    @PutMapping("/{hubId}")
    public ResponseEntity<HubResponseDto> updateHub(
            @PathVariable("hubId") Long hubId,
            @Valid @RequestBody HubRequestDto hubRequestDto
    ){
        return ResponseEntity.ok(hubService.updateHub(hubId, hubRequestDto));
    }

    //허브 삭제
    @PreAuthorize("hasAuthority('MASTER')")
    @DeleteMapping("/{hubId}")
    public ResponseEntity deleteHub(@PathVariable("hubId") Long hubId, Principal principal){
        hubService.deleteHub(hubId, principal.getName());
        return ResponseEntity.noContent().build();
    }

    //허브 조회 (단일)
    @GetMapping("/{hubId}")
    public ResponseEntity<HubResponseDto> getHub(@PathVariable("hubId") Long hubId){
        return ResponseEntity.ok(hubService.getHub(hubId));
    }

    //허브 리스트 조회
    @GetMapping
    public ResponseEntity<Page<HubResponseDto>> getHubs(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc
    ) {
        return ResponseEntity.ok(hubService.getHubs(page, size, sortBy, isAsc));
    }

    //허브 검색
    @GetMapping("/search")
    public ResponseEntity<List<HubResponseDto>> getHubSearch(@RequestParam("keyword") String keyword){
        return ResponseEntity.ok(hubService.getHubSearch(keyword));
    }
}
