package com.george.notification.exception;

public class NotificationSendException extends RuntimeException{
    public NotificationSendException(String message){
        super(message);
    }
}
