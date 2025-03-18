package com.tms.tms.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //403 Forbidden
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "해당 요청에 대한 권한이 없습니다."),
    CANT_MODIFY_ORDER(HttpStatus.FORBIDDEN, "주문 수정이 불가능 합니다."),
    
    //400 bad request
    DUPLICATED_NAME(HttpStatus.BAD_REQUEST, "중복 폴더명이 이미 존재합니다."),
    ALREADY_EXIST_STORE(HttpStatus.BAD_REQUEST, "이미 업체가 존재하는 유저입니다"),
    ALREADY_CANCELED_ORDER(HttpStatus.BAD_REQUEST, "이미 취소된 주문입니다."),
    ALREADY_COMPLETED_ORDER(HttpStatus.BAD_REQUEST, "이미 완료된 주문입니다."),
    ALREADY_IN_DELIVERY(HttpStatus.BAD_REQUEST, "이미 배송중인 주문입니다."),

    //404 not found
    NOT_FOUND_HUB(HttpStatus.NOT_FOUND, "허브가 존재하지 않습니다."),
    NOT_FOUND_STORE(HttpStatus.NOT_FOUND, "업체가 존재하지 않습니다."),
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    NOT_FOUND_CART(HttpStatus.NOT_FOUND, "장바구니가 존재하지 않습니다."),
    NOT_FOUND_CART_PRODUCT(HttpStatus.NOT_FOUND, "장바구니_상품이 존재하지 않습니다."),
    NOT_FOUND_DELIVERY(HttpStatus.NOT_FOUND, "배송 내역이 존재하지 않습니다.")

    ;

    private final HttpStatus status;
    private final String message;

}
