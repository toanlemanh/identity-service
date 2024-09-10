package com.example.identity_service.exception;

public class DuplicationException extends RuntimeException{

    public DuplicationException(String error) {
        super(error);
    }
}
