package com.george.clients.user;

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
