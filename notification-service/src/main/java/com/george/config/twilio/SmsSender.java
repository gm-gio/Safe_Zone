package com.george.config.twilio;

import com.george.clients.user.UserResponse;
import com.george.dto.NotificationRequest;

public interface SmsSender {

    void sendSms(NotificationRequest notificationRequest, UserResponse userResponse);
}
