package com.george.notification.service;

import com.george.notification.dto.response.NotificationHistoryResponse;

public interface NotificationHistoryService {
    NotificationHistoryResponse setNotificationAsASent(Long userId, Long notificationId);
    NotificationHistoryResponse setNotificationAsFailed(Long userId, Long notificationID);
    NotificationHistoryResponse setNotificationAsCorrupt(Long userId, Long notificationId);

}
