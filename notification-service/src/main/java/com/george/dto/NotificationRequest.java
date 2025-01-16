package com.george.dto;

import com.george.enums.NotificationStatus;
import com.george.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationRequest {
    private String message;
    private NotificationType type;
    private NotificationStatus status;
    private Integer retryAttempts;
    private LocalDateTime createdAt;
}
