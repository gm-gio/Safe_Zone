package com.george.user.entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

    USER, ADMIN, GUEST, MODERATOR;

    @Override
    public String getAuthority() {
        return "";
    }
}