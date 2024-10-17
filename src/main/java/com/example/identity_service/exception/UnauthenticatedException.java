package com.example.identity_service.exception;

public class UnauthenticatedException extends AppException{
    public UnauthenticatedException (ErrorCode errorCode) {
        super(errorCode);
    }
}
