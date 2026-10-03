package com.george.notification.service;

import com.george.core.NotificationKafka;
import com.george.notification.dto.request.NotificationRequest;
import com.george.notification.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);

    String distributeNotifications(Long notificationId);

    List<NotificationKafka> getNotificationsForRebalancing(Long pendingSec, Long newSec, Integer size);

    NotificationResponse setNotificationAsPending(Long NotificationId);
}
