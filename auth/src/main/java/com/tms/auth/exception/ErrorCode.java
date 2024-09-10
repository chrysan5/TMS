package com.tms.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //403 Forbidden
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "해당 요청에 대한 권한이 없습니다."),

    //400 bad request
    //DUPLICATED_NAME(HttpStatus.BAD_REQUEST, "중복폴더명이 이미 존재합니다."),

    //404 not found
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;

}
