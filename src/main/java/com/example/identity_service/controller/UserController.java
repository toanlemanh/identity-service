package com.example.identity_service.controller;

import com.example.identity_service.dto.response.ApiResponse;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.IdenUser;
import com.example.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/users")
public class UserController {

   // remove Autowired
    UserService userService;
    @PostMapping("")
    public ResponseEntity<ApiResponse> createUser (@RequestBody @Valid UserCreationRequest request, UriComponentsBuilder ucb){
        // dispatch service
        IdenUser user = userService.createUser(request);
            // build URL
            URI url = ucb
                    .path("/users/{id}")
                    .buildAndExpand(user.getId())
                    .toUri();

            // prepare body with code 1000
            ApiResponse response = new ApiResponse();
            return ResponseEntity.created(url).body(response);
    }

    @GetMapping("")
    public ApiResponse<List<IdenUser>> listUsers () {
        ApiResponse<List<IdenUser>> response = new ApiResponse<>();
        List<IdenUser> users = userService.listUsers();
        response.setResult( users );
        return response;
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<IdenUser> getUserById (@PathVariable String id) {
//        IdenUser user = userService.getUserById(id);
//        return ResponseEntity.ok( user );
//        // else throw 404 in exception folder
//    }
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById (@PathVariable String id){
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setResult( userService.getUserById(id) );
        return response;
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUserById (@PathVariable String id, @RequestBody @Valid UserUpdateRequest request){
        // dispatch
        UserResponse user = userService.updateUserById(id, request);
        ApiResponse<UserResponse> response = new ApiResponse<>();
        // if 204 NO CONTENT automatically then no content is added => switch to 200
        response.setResult(user);
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
