package com.george.notification.service;

import com.george.notification.dto.kafka.NotificationKafka;
import com.george.notification.dto.request.NotificationRequest;
import com.george.notification.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);

    String distributeNotifications(Long notificationId);

    List<NotificationKafka> getNotificationsForRebalancing(Long pendingSec, Long newSec, Integer size);

    NotificationResponse sendNotificationToUser(Long userId, Long notificationId);

    NotificationResponse sendNotificationToGroup(Long groupId, Long notificationId);

    NotificationResponse setNotificationAsASent(Long userId, Long NotificationId);
    NotificationResponse setNotificationAsFailed(Long userId, Long NotificationId);
    NotificationResponse setNotificationAsPending(Long NotificationId);
}
