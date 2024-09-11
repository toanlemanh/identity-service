package com.example.identity_service.exception;

/*
  @Description: This class is for error code and message representations
 */
public enum ErrorCode {
    USER_EXISTS(4000, "User existed!"),
    INVALID_MESSAGE_KEY (4001, "[DEV] Invalid message key!"),
    INVALID_USERNAME(4002, "Username must be at least 8 characters!"),
    INVALID_PASSWORD(4003, "Password must be at least 8 characters!"),
    USER_NOT_FOUND(4004, "User is not found!"),
    UNCATEGORIZED_ERROR (4444, "Uncategorized error !")

    ;

    private int code;
    private String errorMessage;

    ErrorCode(int errorCode, String errorMessage) {
        this.code = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int errorCode) {
        this.code = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
