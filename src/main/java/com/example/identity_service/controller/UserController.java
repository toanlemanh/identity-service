package com.example.identity_service.controller;

import com.example.identity_service.dto.response.ApiResponse;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.IdenUser;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

   // remove Autowired
    UserService userService;
    @PostMapping("")
    public ResponseEntity<ApiResponse> createUser (@RequestBody @Valid UserCreationRequest request, UriComponentsBuilder ucb){
        // dispatch service
        UserResponse userResponse = userService.createUser(request);
            // build URL
            URI url = ucb
                    .path("/users/{id}")
                    .buildAndExpand(userResponse.getId())
                    .toUri();

            // prepare body with code 1000
            ApiResponse response = new ApiResponse();
            response.setResult(userResponse);
            return ResponseEntity.created(url).body(response);
    }

    /**
     * Endpoint belongs to ADMIN => check authorities first
     * To know who are authenticated in the system => use SecurityContextHolder => getContext => getAuthentication
     * Authentication => getName, getAuthorities
     * @return
     */
    @GetMapping("")
    public ApiResponse<Stream> listUsers () {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username >>" + authentication.getName());
//        Print the list of authorities
        authentication.getAuthorities().forEach(
                grantedAuthority -> log.info("Granted authority >> "+ grantedAuthority)
        );
        ApiResponse response = new ApiResponse();
        Stream users = userService.listUsers();
        response.setResult( users );
        return response;
    }

    @GetMapping("/{id}")
//    Chi cho phep user xem duoc dung thong tin cua minh
//    Check jwt claim set => issuer => get id === id
    public ApiResponse<UserResponse> getUserById (@PathVariable String id){
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setResult( userService.getUserById(id) );
        return response;
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUserById (@PathVariable String id, @RequestBody @Valid UserUpdateRequest request){
        // dispatch
        UserResponse userResponse = userService.updateUserById(id, request);
        String after = userResponse.getFirstName() + userResponse.getLastName()
                + userResponse.getDob();
        String before = request.getFirstName() + request.getLastName()
                + request.getDob();
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setResult(userResponse);
        // if 204 NO CONTENT automatically then no content is added => switch to 200
        if (before.equals(after)) {
            response.setMessage(ErrorCode.NO_CONTENT.getErrorMessage());
            response.setCode(ErrorCode.NO_CONTENT.getCode());
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUserById (@PathVariable String id){
        ApiResponse response = new ApiResponse();
        userService.deleteUserById( id );
        // if 204 NO CONTENT automatically then no content is added => switch to 200
        return response;
    }
}
