package com.george.exception;

public class NotificationSendException extends RuntimeException{
    public NotificationSendException(String message){
        super(message);
    }
}
