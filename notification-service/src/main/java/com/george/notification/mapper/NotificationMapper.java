package com.george.notification.mapper;

import com.george.notification.dto.NotificationRequest;
import com.george.notification.dto.NotificationResponse;
import com.george.notification.entity.Notification;
import com.george.notification.entity.NotificationHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse mapToResponse(Notification notification);

    @Mapping(target = "notificationId", ignore = true)
    Notification mapToEntity(NotificationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "templateId", source = "notification.templateId")
    @Mapping(target = "userId", source = "notification.userId")
    @Mapping(target = "groupId", source = "notification.groupId")
    NotificationHistory mapToHistory(Notification notification);
}
