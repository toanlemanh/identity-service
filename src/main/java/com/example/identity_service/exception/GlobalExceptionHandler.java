package com.example.identity_service.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DuplicationException.class)
    public ResponseEntity<String> handlingDuplicationException( DuplicationException exception ){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<String> handlingNotFoundException( NotFoundException exception ){
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(exception.getMessage());
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<String> handlingValidationException( MethodArgumentNotValidException exception ){
        return ResponseEntity.badRequest().body(exception.getFieldError().getDefaultMessage());
    }


}
