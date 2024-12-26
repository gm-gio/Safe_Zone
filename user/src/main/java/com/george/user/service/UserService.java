package com.george.user.service;

import com.george.user.dto.UserRequest;
import com.george.user.dto.UserResponse;
import com.george.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {


    @Transactional
    UserResponse registerUser(UserRequest request);


    UserResponse updateUser(Long userId, UserRequest request);

    @Transactional(readOnly = true)
    List<User> getAllUsers();

    @Transactional(readOnly = true)
    UserResponse getUserById(Long userId);
}
