package com.george.notification.config.twilio;

import com.george.clients.user.UserResponse;

public interface SmsSender {

    void sendSms(String message, UserResponse userResponse);
}
