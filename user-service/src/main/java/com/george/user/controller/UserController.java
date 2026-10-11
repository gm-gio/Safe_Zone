package com.george.user.controller;

import com.george.user.dto.*;
import com.george.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Users", description = "Registration, login and user management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;


    @Operation(summary = "Register a new user", description = "Creates a user with role USER and returns a JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(CREATED).body(userService.register(request));
    }


    @Operation(summary = "Login", description = "Returns a JWT for valid credentials")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }


    @Operation(summary = "Update a user by ID", description = "ADMIN or the user themselves",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "403", description = "Not allowed"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID of the user") @PathVariable("userId") Long userId,
            @Valid @RequestBody UserRequest request) {

        return ResponseEntity.status(OK).body(userService.updateUser(userId, request));
    }


    @Operation(summary = "Receive all users", description = "ADMIN only",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users"),
            @ApiResponse(responseCode = "403", description = "Not allowed")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.status(OK).body(userService.getAllUsers());
    }


    @Operation(summary = "Receive a user by ID", description = "ADMIN or the user themselves",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "403", description = "Not allowed"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID of the user") @PathVariable("userId") Long userId) {

        return ResponseEntity.status(OK).body(userService.getUserById(userId));
    }


    @Operation(summary = "Delete a user by ID", description = "ADMIN or the user themselves",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "403", description = "Not allowed"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID of the user") @PathVariable("userId") Long userId) {

        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Check if a user exists by email", description = "Internal use (other services)")
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkUserExistsByEmail(
            @Parameter(description = "User email") @RequestParam("email") String email) {

        return ResponseEntity.ok(userService.isUserExistsByEmail(email));
    }


    @Operation(summary = "Get all user IDs", description = "Internal use (notification service). Blocked on the gateway")
    @GetMapping("/ids")
    public List<Long> getUserIds() {
        return userService.getAllUserIds();
    }
}