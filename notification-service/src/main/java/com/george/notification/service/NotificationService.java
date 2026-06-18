package com.george.notification.service;

import com.george.notification.dto.request.NotificationRequest;
import com.george.notification.dto.response.NotificationResponse;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);

    String distributeNotifications(Long notificationId);

    NotificationResponse sendNotificationToUser(Long userId, Long notificationId);

    NotificationResponse sendNotificationToGroup(Long groupId, Long notificationId);

    NotificationResponse setNotificationAsASent(Long userId, Long NotificationId);
    NotificationResponse setNotificationAsFailed(Long userId, Long NotificationId);
    NotificationResponse setNotificationAsPending(Long NotificationId);
}
