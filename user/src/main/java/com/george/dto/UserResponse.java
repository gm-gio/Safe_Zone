package com.george.dto;

import com.george.user.UserRole;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole userRole;
}
