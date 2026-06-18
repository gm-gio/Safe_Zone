package com.george.notification.dto.kafka;

import com.george.notification.enums.NotificationStatus;
import com.george.notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationKafka {
    private Long id;
    private NotificationType type;
    private String credential;
    private NotificationStatus status;
    private Integer retryAttempts;
    private Long userId;

}
