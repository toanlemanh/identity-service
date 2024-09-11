package com.example.identity_service.exception;

/*
  @Description: This class is for error code and message representations
 */
public enum ErrorCode {
    USER_EXISTS(4000, "User existed!"),
    INVALID_USER_FIELD(4001, "Invalid field!"),
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
