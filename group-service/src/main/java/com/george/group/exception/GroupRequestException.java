package com.george.group.exception;

public class GroupRequestException extends RuntimeException {
    public GroupRequestException(String message) {
        super(message);
    }

    public GroupRequestException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
