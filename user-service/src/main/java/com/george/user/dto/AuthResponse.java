package com.george.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.george.user.entity.UserRole;


@Data
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private Long userId;
    private String email;
    private UserRole role;
}