package com.george.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ErrorMessage(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
}
