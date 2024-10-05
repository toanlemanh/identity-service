package com.example.identity_service.controller;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.response.ApiResponse;
import com.example.identity_service.dto.response.AuthenticationResponse;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticated (@RequestBody AuthenticationRequest request){
        boolean authenticated = authenticationService.authenticate(request);
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>();
        response.setResult( AuthenticationResponse
                .builder()
                .authenticated(authenticated)
                .build());
        return response;
    }
}
