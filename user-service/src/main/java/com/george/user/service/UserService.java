package com.george.user.service;

import com.george.user.dto.*;

import java.util.List;

public interface UserService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse updateUser(Long userId, UserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long userId);

    void deleteById(Long userId);

    boolean isUserExistsByEmail(String email);

    List<Long> getAllUserIds();
}
