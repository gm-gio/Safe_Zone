package com.george.service.impl;

import com.george.dto.UserRequest;
import com.george.repository.UserRepository;

import com.george.service.UserService;
import com.george.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void registerUser(UserRequest request) {
    User user = User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .passwordHash(request.getPasswordHash())
            .userRole(request.getUserRole())
            .build();

    userRepository.save(user);
    }

    public User getById(Long userId) throws ChangeSetPersister.NotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not fnd"));
    }

}
