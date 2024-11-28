package com.example.identity_service.configuration;

import com.example.identity_service.dto.response.ApiResponse;
import com.example.identity_service.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorCode error = ErrorCode.UNAUTHENTICATED;
        response.setStatus(error.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(error.getCode());
        apiResponse.setMessage(error.getErrorMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString( apiResponse );
        //convert obj to json (string)
        //write body
        response.getWriter().write(body);
        response.flushBuffer();
    }
}
