package com.george.user.service.impl;


import com.george.core.UserRegisterEvent;
import com.george.user.dto.UserRequest;


import com.george.user.dto.UserResponse;

import com.george.user.entity.UserRole;
import com.george.user.exception.UserNotFoundException;
import com.george.user.exception.UserRegistrationException;
import com.george.user.mapper.UserMapper;
import com.george.user.repository.UserRepository;

import com.george.user.service.UserService;
import com.george.user.entity.User;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserRegisterEvent> kafkaTemplate;
    private final UserMapper mapper;

    @Value("${spring.kafka.topics.user-register}")
    private String userRegisterTopic;


    @Override
    @Transactional
    public UserResponse registerUser(UserRequest request) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

            if (existingUser.isPresent()) {
                throw new UserRegistrationException("User already exists");
            }

            User user = mapper.mapToEntity(request);
            user.setUserRole(UserRole.USER);

            User savedUser = userRepository.save(user);

            sendUserRegistrationEvent(savedUser);

            return mapper.mapToResponse(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new UserRegistrationException("Database error during user registration", e);
        }
    }



    private void sendUserRegistrationEvent(User user) {
        UserRegisterEvent event = new UserRegisterEvent(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );

        kafkaTemplate.send(userRegisterTopic, event);

    }

    @Override
    @Transactional
    public UserResponse updateUser(Long userId, UserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());


        User updatedUser = userRepository.save(user);
        return mapper.mapToResponse(updatedUser);
    }




//    @Override
//    public void registerUser(UserRequest request) {
//    User user = User.builder()
//            .firstName(request.getFirstName())
//            .lastName(request.getLastName())
//            .email(request.getEmail())
//            .phone(request.getPhone())
//            .passwordHash(request.getPasswordHash())
//            .userRole(request.getUserRole())
//            .build();
//
//    userRepository.save(user);
//    }



    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Transactional(readOnly = true)
    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return mapper.mapToResponse(user);
    }


}