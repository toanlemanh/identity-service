package com.example.identity_service.service;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.entity.IdenUser;
import com.example.identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public IdenUser createUser (UserCreationRequest request) {
        // handle request
        IdenUser user = new IdenUser();

        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user.setDob(request.getDob());
        //save and return entity to Controller
        return userRepository.save(user);
    }

    public List<IdenUser> listUsers () {
        return userRepository.findAll();
    }

    public IdenUser getUserById (String id) {
        return userRepository.findById(id).get();
    }

}
