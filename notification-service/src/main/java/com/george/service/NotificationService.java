package com.george.service;

import com.george.dto.NotificationRequest;
import com.george.dto.NotificationResponse;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);

    NotificationResponse sendNotification(Long userId, Long notificationId);
}
