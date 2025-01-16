package com.george.mapper;

import com.george.dto.NotificationRequest;
import com.george.dto.NotificationResponse;
import com.george.entity.Notification;
import com.george.entity.NotificationHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse mapToResponse(Notification notification);

    @Mapping(target = "notificationId", ignore = true)
    Notification mapToEntity(NotificationRequest request);

    @Mapping(target = "id", ignore = true)
    NotificationHistory mapToHistory(Notification notification);
}
