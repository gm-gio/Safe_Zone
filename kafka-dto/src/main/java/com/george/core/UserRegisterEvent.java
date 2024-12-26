package com.george.core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterEvent {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
}
