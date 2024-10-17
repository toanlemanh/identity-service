package com.example.identity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * This response for checking the validation of token (expired time, hackers, ..
 * After receiving the token from the client => check it and return the validation
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class IntrospectResponse {
    boolean validate;
}
