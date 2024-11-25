package com.george.controller;



import com.george.clients.user.UserClient;
import com.george.clients.user.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final UserClient userClient;

    @GetMapping("/test-user/{userId}")
    public ResponseEntity<UserResponse> getUserForGroup(@PathVariable Long userId) {
        UserResponse userResponse = userClient.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }
}
