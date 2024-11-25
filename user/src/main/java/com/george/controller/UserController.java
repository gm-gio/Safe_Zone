package com.george.controller;


import com.george.dto.UserRequest;
import com.george.exception.UserNotFoundException;
import com.george.exception.UserRequestException;
import com.george.service.UserService;
import com.george.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class  UserController {

    private final UserService userService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody UserRequest userRequest) {
        userService.registerUser(userRequest);
    }


    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
//        throw  new UserRequestException("ops cannot get all uss/  with custom Exception");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

}
