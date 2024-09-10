package com.tms.tms.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TmsCustomException.class)
    public ResponseEntity tmsCustomException(TmsCustomException ex) {
        log.error("Error occurs in {}", ex.toString());
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getStatus(), ex.getErrorCode().getMessage());
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }
}
