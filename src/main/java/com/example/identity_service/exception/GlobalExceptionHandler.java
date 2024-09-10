package com.example.identity_service.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DuplicationException.class)
    public ResponseEntity<String> handlingRuntimeException( DuplicationException exception ){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<String> handlingRuntimeException( NotFoundException exception ){
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(exception.getMessage());
    }

}
