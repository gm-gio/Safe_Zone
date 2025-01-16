package com.george.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class NotificationExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGenericException(Exception e) {

        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), internalServerError, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value = {NotificationNotFoundException.class})
    public ResponseEntity<Object> handlerUserNotFoundException(NotificationNotFoundException e) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;

        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), notFound, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
