package com.george.clients.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "USER")
public interface UserClient {

    @GetMapping("/api/v1/users/{userId}")
    public UserResponse getUserById(@PathVariable Long userId);

    @GetMapping("/api/v1/users/")
    ResponseEntity<List<UserResponse>> getUsers();

    @PostMapping("/api/v1/users/register")
    ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest);

    @GetMapping("/api/v1/users/exists")
    ResponseEntity<Boolean> checkUserExistsByEmail(@RequestParam String email);
}
