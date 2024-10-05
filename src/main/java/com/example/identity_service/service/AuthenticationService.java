package com.example.identity_service.service;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.IdenUser;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.exception.NotFoundException;
import com.example.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    public boolean authenticate (AuthenticationRequest request) {
       // find username
       IdenUser user = userRepository.findByUsername(request.getUsername() ).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_EXIST));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
       return encoder.matches(request.getPassword(), user.getPassword());
    }
}
