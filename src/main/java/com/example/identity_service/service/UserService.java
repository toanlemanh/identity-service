package com.example.identity_service.service;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.IdenUser;
import com.example.identity_service.enums.Role;
import com.example.identity_service.exception.DuplicationException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.exception.NotFoundException;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor // Constructor dependency injection => remove @Autowired
//Create UserController constructor inject UserRepository and UserMapper
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// make all field with not accessory being final
public class UserService {
     UserRepository userRepository;
     UserMapper userMapper;

    public UserResponse createUser (UserCreationRequest request) {
        // handle request
        if ( userRepository.existsByUsername(request.getUsername()) )
             throw new DuplicationException(ErrorCode.USER_EXISTS);

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        // mapstruct to binding data between dto
        IdenUser user = userMapper.toUser(request);
        user.setRoles(roles);
        // encrypt password => set password in user entity, not in request ??
        // avoid leaking UserCreationRequest
        PasswordEncoder bcryptEncoder = new BCryptPasswordEncoder(10);
        user.setPassword( bcryptEncoder.encode(request.getPassword()) );
        System.out.println(user.getPassword());
        // Removed manual updating
        //save and return entity to Controller to build header location

        // map IdenUser back to DTO (UserResponse) then return it
        return userMapper.toUserResponse( userRepository.save(user));
    }

    public Stream listUsers () {
        return userRepository.findAll().stream().map(user -> userMapper.toUserResponse(user));
    }

    public UserResponse getUserById (String id) {
       return userMapper.toUserResponse(
               //find IdenUser first then map to UserResponse (dto)
               userRepository
                       .findById(id)
                       .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND)));
    }
    public UserResponse getUserByName (String name) {
        return userMapper.toUserResponse(
                //find IdenUser first then map to UserResponse (dto)
                userRepository
                        .findByUsername(name)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse updateUserById (String id, UserUpdateRequest request) {
        //this endpoint does not allow update the username, role and password
        IdenUser user = userRepository
                            .findById(id)
                            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        // But we cannot set the role here => need to check the requested role
        // and the current privilege role
        // if wrong user id, throw exception above
        // else map data request to entity using mapstruct
        userMapper.toUpdateUser(user, request);
        // continue map entity to dto response
        UserResponse response = userMapper.toUserResponse(userRepository.save(user));
        return response;
    }

    // Hard delete
    public void deleteUserById (String id) {
        if ( userRepository.existsById(id) ){
            // delete by id
            userRepository.deleteById(id);
        }
        else throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
    }

}
