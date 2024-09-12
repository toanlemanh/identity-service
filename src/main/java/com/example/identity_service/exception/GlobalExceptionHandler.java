package com.example.identity_service.exception;

import com.example.identity_service.dto.response.ApiResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    /*
     *@DESCRIPTION: Fallback exception: UNCATEGORIZED EXCEPTION (NULL, SYNTAX, NETWORK OR WHATEVER HAS NOT DEFINED)
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception ){
        ApiResponse response = new ApiResponse();
        response.setCode( ErrorCode.UNCATEGORIZED_ERROR.getCode() );
        response.setMessage( ErrorCode.UNCATEGORIZED_ERROR.getErrorMessage() );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = DuplicationException.class)
    public ResponseEntity<ApiResponse> handlingDuplicationException( DuplicationException exception ){
        ApiResponse response = new ApiResponse();
        response.setCode( exception.getErrorCode().getCode() );
        response.setMessage( exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ApiResponse> handlingNotFoundException( NotFoundException exception ){
        ApiResponse response = new ApiResponse();
        response.setCode( exception.getErrorCode().getCode());
        response.setMessage( exception.getMessage() );
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body( response );
    }
    /*
    * @Description: This exception is not customised or configured */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handlingValidationException( MethodArgumentNotValidException exception ){
        ApiResponse response = new ApiResponse();

        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_MESSAGE_KEY;

        try {
            // check valueOf(enumKey) be a exception/error
            // if invalid => stop and catch while errorCode is still Invalid
            // else => assign
            errorCode = ErrorCode.valueOf(enumKey);
        }
        catch (IllegalArgumentException e){
            e.toString();
        }

        response.setCode( errorCode.getCode() );
        // get the specific error message from @Element (Size)
        response.setMessage( errorCode.getErrorMessage() );
        return ResponseEntity.badRequest().body(response);
    }


}
