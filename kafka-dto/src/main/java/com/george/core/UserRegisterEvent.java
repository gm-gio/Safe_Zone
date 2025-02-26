package com.george.core;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterEvent {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
