package com.george.notification.enums;

public enum NotificationType implements EnumCode {

    EMAIL("EML"),  //Amazon SES (Simple Email Service
    PHONE("PHN"),  //Twilio
    PUSH("PSH");   //Apple Push Notification Service (APNs):

    private final String code;

    NotificationType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
