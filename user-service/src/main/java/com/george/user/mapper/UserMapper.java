package com.george.user.mapper;

import com.george.user.dto.UserRequest;
import com.george.user.dto.UserResponse;
import com.george.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse mapToResponse(User user);

    @Mapping(target = "userId", ignore = true)
    User mapToEntity(UserRequest userRequest);
}
