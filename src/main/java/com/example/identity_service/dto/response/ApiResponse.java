package com.example.identity_service.dto.response;

// This class for refining a standard API response for this service
// provide user with data integrity and error handling
// Don't include null field

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data; // include RequireArgsConstructor
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL) // filter null , only approve non-null
@Setter
@Getter
public class ApiResponse<T> {
    //success request by default
    private int code = 1000;
    private String message;
    private T result;

}
