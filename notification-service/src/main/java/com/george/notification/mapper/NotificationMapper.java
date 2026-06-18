package com.george.notification.mapper;

import com.george.notification.dto.kafka.NotificationKafka;
import com.george.notification.dto.request.NotificationRequest;
import com.george.notification.dto.response.NotificationResponse;
import com.george.notification.entity.Notification;
import com.george.notification.entity.NotificationHistory;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

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
    NotificationKafka mapToKafka(NotificationResponse notificationResponse, @Context Map<String, String> urlOptionMap);


}
