package com.example.identity_service.mapper;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.IdenUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

//generate mapper using in Spring (DI)
// to be autowired mapper
@Mapper(componentModel = "spring")
public interface UserMapper {
    // map Creation request to entity
    IdenUser toUser(UserCreationRequest request);
    void toUpdateUser(@MappingTarget IdenUser user, UserUpdateRequest request);

    // map entity to dto response
    UserResponse toUserResponse(IdenUser user);

    //Mapping when source is different from target attribute
    //@Mapping("firstName", "lastName")

    //OR you want to ignore an attribute (not convert this attribute)
    //@Mapping("targetAttribute", ignore = true)

    //

}
