package com.george.user.service.impl;

import com.george.core.UserRegisterEvent;
import com.george.core.UserRemoveEvent;
import com.george.core.UserUpdateEvent;
import com.george.user.dto.UserRequest;

import com.george.user.dto.UserResponse;
import com.george.user.entity.UserRole;
import com.george.user.exception.UserNotFoundException;
import com.george.user.exception.UserRegistrationException;
import com.george.user.exception.UserRequestException;
import com.george.user.mapper.UserMapper;
import com.george.user.repository.UserRepository;
import com.george.user.service.UserService;
import com.george.user.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserRegisterEvent> userRegisterKafkaTemplate;
    private final KafkaTemplate<String, UserUpdateEvent> userUpdateKafkaTemplate;
    private final KafkaTemplate<String, UserRemoveEvent> userRemoveKafkaTemplate;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${spring.kafka.topics.user-register}")
    private String userRegisterTopic;

    @Value("${spring.kafka.topics.user-remove}")
    private String userRemoveTopic;

    @Value("${spring.kafka.topics.user-update}")
    private String userUpdateTopic;

    @Override
    @Transactional
    public UserResponse registerUser(UserRequest request) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

            if (existingUser.isPresent()) {
                throw new UserRequestException("User with Email: " + request.getEmail() + "already exists");
            }

            String hashedPassword = passwordEncoder.encode(request.getPasswordHash());

            User user = mapper.mapToEntity(request);
            user.setPasswordHash(hashedPassword);
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
                user.getEmail(),
                user.getPhone()
        );
        userRegisterKafkaTemplate.send(userRegisterTopic, event);
    }

    @Transactional
    @Override
    public UserResponse updateUser(Long userId, UserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        User updatedUser = userRepository.save(user);

        UserUpdateEvent event = new UserUpdateEvent(user.getUserId(), user.getFirstName(), user.getLastName(), user.getPhone());
        userUpdateKafkaTemplate.send(userUpdateTopic, event);

        return mapper.mapToResponse(updatedUser);
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(mapper::mapToResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return mapper.mapToResponse(user);
    }


    @Override
    @Transactional
    public void deleteById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        UserRemoveEvent event = new UserRemoveEvent(user.getUserId());

        userRemoveKafkaTemplate.send(userRemoveTopic, event);

        userRepository.delete(user);
    }

    @Override
    public boolean isUserExistsByEmail(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.isPresent();
    }


    @Override
    public List<Long> getAllUserIds() {
        return userRepository.findAllUserIds();
    }

}
