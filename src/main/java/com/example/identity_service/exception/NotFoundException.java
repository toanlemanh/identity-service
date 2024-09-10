package com.example.identity_service.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException (String error){
        super(error);
    }
}
