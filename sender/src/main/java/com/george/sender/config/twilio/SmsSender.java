package com.george.sender.config.twilio;

public interface SmsSender {

    void sendSms(String message, String userResponse);
}
