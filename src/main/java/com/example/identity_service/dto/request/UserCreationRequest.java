package com.example.identity_service.dto.request;

import com.example.identity_service.exception.ErrorCode;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
//= @Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    //Cannot call getErrorCode from Enum ErrorCode.INVALID_USERNAME
    @Size(min = 8, message = "INVALID_USERNAME" )
     String username;
    @Size(min = 8, message = "INVALID_PASSWORD")
     String password;
     String firstName;
     String lastName;
     LocalDate dob;


}
