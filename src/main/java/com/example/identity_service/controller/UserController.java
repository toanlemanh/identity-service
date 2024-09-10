package com.example.identity_service.controller;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.entity.IdenUser;
import com.example.identity_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<Void> createUser (@RequestBody @Valid UserCreationRequest request, UriComponentsBuilder ucb){
        // dispatch
        IdenUser user = userService.createUser(request);
        if (user != null){
            // build URL
            URI url = ucb
                    .path("/users/{id}")
                    .buildAndExpand(user.getId())
                    .toUri();
            return ResponseEntity.created(url).build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("")
    public ResponseEntity<List<IdenUser>> listUsers () {
        List<IdenUser> users = userService.listUsers();
            return ResponseEntity.ok( users );
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdenUser> getUserById (@PathVariable String id) {
        IdenUser user = userService.getUserById(id);
        return ResponseEntity.ok( user );
        // else throw 404 in exception folder
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserById (@PathVariable String id, @RequestBody @Valid UserUpdateRequest request){
        // dispatch
        IdenUser user = userService.updateUserById(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById (@PathVariable String id){
        if (userService.deleteUserById( id )) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
