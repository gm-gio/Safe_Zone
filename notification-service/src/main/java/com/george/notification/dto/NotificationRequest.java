package com.george.notification.dto;

import com.george.notification.enums.NotificationStatus;
import com.george.notification.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationRequest {
    private Long userId;
    private Long templateId;
    private NotificationType type;
    private NotificationStatus status;
    private Integer retryAttempts;
    private LocalDateTime createdAt;
}
