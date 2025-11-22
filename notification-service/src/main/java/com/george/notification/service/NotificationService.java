package com.george.notification.service;

import com.george.notification.dto.NotificationRequest;
import com.george.notification.dto.NotificationResponse;

public interface NotificationService {

    NotificationResponse createAndSetPending(NotificationRequest request);

    String distributeNotifications(Long notificationId);

    NotificationResponse setNotificationAsPending(Long notificationId);

    NotificationResponse sendNotificationToUser(Long userId, Long notificationId);


    NotificationResponse sendNotificationToGroup(Long groupId, Long notificationId);
}
