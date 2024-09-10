package com.tms.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TmsCustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
