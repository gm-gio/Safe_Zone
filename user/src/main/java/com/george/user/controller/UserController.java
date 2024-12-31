package com.george.user.controller;


import com.george.user.dto.UserRequest;
import com.george.user.dto.UserResponse;
import com.george.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    @Operation(summary = "register new a user")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(CREATED).body(userService.registerUser(userRequest));
    }


    @PutMapping("/{userId}")
    @Operation(summary = "update a User by ID")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long userId,
            @RequestBody UserRequest request) {

        return ResponseEntity.status(OK).body(userService.updateUser(userId, request));
    }

    @GetMapping("/")
    @Operation(summary = "receive all Users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.status(OK).body(userService.getAllUsers());
    }


    @GetMapping("/{userId}")
    @Operation(summary = "receive a User  by ID")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.status(OK).body(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "delete a User by ID")
    public ResponseEntity<Void> deleteById(@PathVariable Long userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
