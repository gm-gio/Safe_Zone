package com.george.service;

import com.george.dto.UserRequest;
import com.george.user.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    void registerUser(UserRequest request);

    @Transactional(readOnly = true)
    List<User> getAllUsers();
}
