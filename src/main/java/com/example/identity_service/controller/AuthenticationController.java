package com.example.identity_service.controller;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.dto.response.ApiResponse;
import com.example.identity_service.dto.response.AuthenticationResponse;
import com.example.identity_service.dto.response.IntrospectResponse;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;


    @PostMapping("/token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate (@RequestBody AuthenticationRequest request){
//        Object jwt = authenticationService.authenticate(request);
//        boolean authenticated = false;
//        if (jwt != null) {
//            authenticated = true;
//        }
//        ApiResponse<AuthenticationResponse> body = new ApiResponse<>();
//        body.setResult( AuthenticationResponse
//                            .builder()
//                            .authenticated(authenticated)
//                            .token(String.valueOf(jwt))
//                            .build() );
//        if (jwt != null) return ResponseEntity.status(200).header("Authorization", "Bearer" + jwt).body( body );
//        return ResponseEntity.status(200).body( body );
        AuthenticationResponse result = authenticationService.authenticate(request);
        ApiResponse<AuthenticationResponse> body = new ApiResponse<>();
        body.setResult( result );
        return ResponseEntity.ok().header("Authorization", "Bearer" + result.getToken()).body(body);
    }
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        ApiResponse<IntrospectResponse> response = new ApiResponse<>();
      // check token
        IntrospectResponse result = authenticationService.introspectToken(request);
        response.setResult(result);
        return response;
    }
    @PostMapping("/logout")
    public ApiResponse logout () {
        // remove session

        // redirect to the home page or signin page
        return null;
    }
}
