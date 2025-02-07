package com.george.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateEvent {
    private Long userId;
    private String firstName;
    private String lastName;
    private String phone;
}
