package com.example.identity_service.exception;

public class NotFoundException extends AppException{
    public NotFoundException (ErrorCode errorCode){
        super(errorCode);
    }
}
