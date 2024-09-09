package com.example.identity_service.controller;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.entity.IdenUser;
import com.example.identity_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Void> createUser (@RequestBody UserCreationRequest request, UriComponentsBuilder ucb){
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
        if ( user != null )
          return ResponseEntity.ok( user );
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<IdenUser> updateUserById (@PathVariable String id, @RequestBody UserUpdateRequest request){
        // dispatch
        IdenUser user = userService.updateUserById(id, request);
        if ( user != null ){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById (@PathVariable String id){
        if (userService.deleteUserById( id )) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
