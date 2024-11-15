package com.george.mapper;

import com.george.dto.UserRequest;
import com.george.dto.UserResponse;
import com.george.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
    User toEntity(UserResponse userResponse);

    UserRequest toRequest(User user);
    User toEntity(UserRequest userRequest);
}
