package com.george.user.dto;


import com.george.user.entity.UserRole;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String passwordHash;
    private UserRole userRole;
}
