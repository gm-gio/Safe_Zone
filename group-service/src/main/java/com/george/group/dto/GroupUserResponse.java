package com.george.group.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupUserResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
