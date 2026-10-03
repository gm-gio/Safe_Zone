package com.george.core;


import com.george.core.enums.NotificationStatus;
import com.george.core.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

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
    private Long templateId;
    private Map<String, String> urlOptionMap;


}
