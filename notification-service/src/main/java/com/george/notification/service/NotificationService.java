package com.george.notification.service;

import com.george.notification.dto.NotificationRequest;
import com.george.notification.dto.NotificationResponse;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);

    NotificationResponse sendNotification(Long userId, Long notificationId);


    NotificationResponse sendNotificationToGroup(Long groupId, Long notificationId);
}
