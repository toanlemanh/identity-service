package com.example.identity_service.exception;

/*
* @Description: This class is for exception representation including ErrorCode
* */
public class DuplicationException extends AppException{
    public DuplicationException (ErrorCode errorCode) {
        super(errorCode);
    }
}
