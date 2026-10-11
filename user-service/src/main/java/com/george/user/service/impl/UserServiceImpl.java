package com.george.user.service.impl;

import com.george.core.UserRegisterEvent;
import com.george.core.UserRemoveEvent;
import com.george.core.UserUpdateEvent;
import com.george.user.dto.*;

import com.george.user.entity.UserRole;
import com.george.user.exception.UserAlreadyExistsException;
import com.george.user.exception.UserNotFoundException;
import com.george.user.mapper.UserMapper;
import com.george.user.repository.UserRepository;
import com.george.user.service.JwtService;
import com.george.user.service.UserService;
import com.george.user.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${spring.kafka.topics.user-register}")
    private String userRegisterTopic;

    @Value("${spring.kafka.topics.user-remove}")
    private String userRemoveTopic;

    @Value("${spring.kafka.topics.user-update}")
    private String userUpdateTopic;


    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(
                    "User with email " + email + " already exists"
            );
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(email);
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setUserRole(UserRole.USER);

        User savedUser;
        try {
            savedUser = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException(
                    "User with email " + email + " already exists"
            );
        }

        sendUserRegistrationEvent(savedUser);

        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(
                token,
                savedUser.getUserId(),
                savedUser.getEmail(),
                savedUser.getUserRole()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid email or password")
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                user.getUserId(),
                user.getEmail(),
                user.getUserRole()
        );
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

        userRepository.delete(user);

        userRemoveKafkaTemplate.send(userRemoveTopic, event);
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
