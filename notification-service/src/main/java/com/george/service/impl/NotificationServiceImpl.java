package com.george.service.impl;

import com.george.clients.user.UserClient;
import com.george.clients.user.UserResponse;
import com.george.config.twilio.SmsSender;
import com.george.dto.NotificationRequest;
import com.george.dto.NotificationResponse;
import com.george.entity.Notification;
import com.george.entity.NotificationHistory;
import com.george.enums.NotificationStatus;
import com.george.enums.NotificationType;
import com.george.exception.NotificationSendException;
import com.george.mapper.NotificationMapper;
import com.george.repository.NotificationHistoryRepository;
import com.george.repository.NotificationRepository;
import com.george.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service("twilio")
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;
    private final NotificationMapper mapper;
    private final SmsSender smsSender; //sms sender
    private final UserClient userClient;


    @Override
    public NotificationResponse createNotification(NotificationRequest request) {
        return Optional.of(request)
                .map(mapper::mapToEntity)
                .map(notificationRepository::saveAndFlush)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new IllegalArgumentException("Failed to create notification"));
    }

    @Override
    public NotificationResponse sendNotification(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + notificationId));

        UserResponse user = userClient.getUserById(userId);


        try {
            sendNotificationAndUpdateStatus(notification, user);

        } catch (NotificationSendException e) {
            handleNotificationSendingFailure(notification);
        } finally {
            notificationRepository.save(notification);
            handleNotificationHistory(notification);
        }

        return mapper.mapToResponse(notification);
    }

    private void sendNotificationAndUpdateStatus(Notification notification, UserResponse user) {
        NotificationRequest request = NotificationRequest.builder()
                .message(notification.getMessage())
                .type(NotificationType.PHONE)
                .status(NotificationStatus.IN_PROGRESS)
                .retryAttempts(notification.getRetryAttempts())
                .createdAt(LocalDateTime.now())
                .build();

        //Simulated SMS sending
        sendSmsNotification(request, user);

         //smsSender.sendSms(request, user);
        //Twilio sms sender

        notification.setStatus(NotificationStatus.DELIVERED);
    }

    private void handleNotificationSendingFailure(Notification notification) {
        notification.setRetryAttempts(notification.getRetryAttempts() + 1);

        if (notification.getRetryAttempts() >= 3) {
            notification.setStatus(NotificationStatus.FAILED);
        } else {
            notification.setStatus(NotificationStatus.RESENDING);
        }
    }


    private void handleNotificationHistory(Notification notification) {
        if (notification.getStatus() == NotificationStatus.DELIVERED || notification.getStatus() == NotificationStatus.FAILED) {
            NotificationHistory history = mapper.mapToHistory(notification);
            notificationHistoryRepository.save(history);
            notificationRepository.delete(notification);
        }
    }


    private void sendSmsNotification(NotificationRequest request, UserResponse user) {

        if (Math.random() > 0.7) {
            throw new NotificationSendException("Simulated SMS sending failure");
        }
    }
}




