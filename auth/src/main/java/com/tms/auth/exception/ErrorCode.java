package com.tms.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //403 Forbidden
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "해당 요청에 대한 권한이 없습니다."),
    NOT_PERMITTED(HttpStatus.BAD_REQUEST, "잘못된 접근입니다"),

    //400 bad request
    //DUPLICATED_NAME(HttpStatus.BAD_REQUEST, "중복폴더명이 이미 존재합니다."),

    //404 not found
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    NOT_FOUND_DELIVERY_USER(HttpStatus.NOT_FOUND, "delivery user가 존재하지 않습니다."),

    DELIVERYTYPE_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST, "배송담당자 소속 필수"),
    HUBID_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST, "허브아이디 필수")
    ;

    private final HttpStatus status;
    private final String message;

}
