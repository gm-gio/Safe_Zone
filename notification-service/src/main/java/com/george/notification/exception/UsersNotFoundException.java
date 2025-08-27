package com.george.notification.exception;

import jakarta.persistence.EntityNotFoundException;

public class UsersNotFoundException extends EntityNotFoundException {
    public UsersNotFoundException(String message) {
        super(message);
    }
}
