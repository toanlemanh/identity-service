package com.example.identity_service.service;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.entity.IdenUser;
import com.example.identity_service.exception.DuplicationException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.exception.NotFoundException;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public IdenUser createUser (UserCreationRequest request) {
        // handle request
        if ( userRepository.existsByUsername( request.getUsername() ) )
             throw new DuplicationException(ErrorCode.USER_EXISTS);

        // mapstruct to binding data between dtos
        IdenUser user = userMapper.toUser(request);
        // Removed manual updating
        //save and return entity to Controller
        return userRepository.save(user);
    }

    public List<IdenUser> listUsers () {
        return userRepository.findAll();
    }

    public IdenUser getUserById (String id) {
       return userRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public void updateUserById (String id, UserUpdateRequest request) {
        IdenUser user = getUserById(id);
        // if not user, throw exception above
        if ( user != null ) {
            userMapper.toUpdateUser(user, request);
            //removed boilerplate code, leave it for mapstruct
            userRepository.save(user);
        }
    }

    // Hard delete
    public boolean deleteUserById (String id) {
        if ( userRepository.existsById(id) ){
            // delete by id
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
