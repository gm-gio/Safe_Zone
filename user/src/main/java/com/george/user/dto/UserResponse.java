package com.george.user.dto;

import com.george.user.entity.UserRole;
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
