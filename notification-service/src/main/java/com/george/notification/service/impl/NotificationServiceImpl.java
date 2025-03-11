package com.george.notification.service.impl;

import com.george.clients.group.GroupClient;
import com.george.clients.group.GroupUserResponse;
import com.george.clients.template.TemplateClient;
import com.george.clients.template.TemplateResponse;
import com.george.clients.user.UserClient;
import com.george.clients.user.UserResponse;
import com.george.notification.config.twilio.SmsSender;
import com.george.notification.dto.NotificationRequest;
import com.george.notification.dto.NotificationResponse;
import com.george.notification.entity.Notification;
import com.george.notification.entity.NotificationHistory;
import com.george.notification.enums.NotificationStatus;
import com.george.notification.exception.NotificationNotFoundException;
import com.george.notification.exception.NotificationSendException;
import com.george.notification.mapper.NotificationMapper;
import com.george.notification.repository.NotificationHistoryRepository;
import com.george.notification.repository.NotificationRepository;
import com.george.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service("twilio")
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;
    private final NotificationMapper mapper;
    private final SmsSender smsSender; //sms sender
    private final UserClient userClient;
    private final TemplateClient templateClient;
    private final GroupClient groupClient;

    @Autowired
    private final JavaMailSender javaMailSender;  //AWS


    @Override
    public NotificationResponse createNotification(NotificationRequest request) {

        TemplateResponse template = templateClient.getTemplateById(request.getTemplateId());


        Notification notification = Notification.builder()
                .templateId(request.getTemplateId())
                .type(request.getType())
                .status(NotificationStatus.NEW)
                .retryAttempts(0)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            notificationRepository.save(notification);
        } catch (Exception e) {
            log.error("Error saving notification: {}", e.getMessage());
            throw new NotificationSendException("Could not create notification");
        }

        return mapper.mapToResponse(notification);
    }


    @Override
    public NotificationResponse sendNotification(Long userId, Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));


        UserResponse user = userClient.getUserById(userId);
        TemplateResponse template = templateClient.getTemplateById(notification.getTemplateId());
        String content = template.getContent();

        try {
            sendNotificationAndUpdateStatus(notification, user, content);
        } catch (NotificationSendException e) {
            handleNotificationSendingFailure(notification);
        } finally {

            notificationRepository.save(notification);
            handleNotificationHistory(notification);
        }

        return mapper.mapToResponse(notification);
    }


    @Override
    public NotificationResponse sendNotificationToGroup(Long groupId, Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));


        List<GroupUserResponse> groupUsers = groupClient.getUsersByGroupId(groupId);


        TemplateResponse template = templateClient.getTemplateById(notification.getTemplateId());
        String content = template.getContent();
        String title = template.getTitle();


        for (GroupUserResponse user : groupUsers) {
            try {
                if (user.getEmail() != null) {

                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom("giorgimeshve@gmail.com");
                    message.setTo(user.getEmail());
                    message.setSubject(title);
                    message.setText(content);


                    javaMailSender.send(message);
                    notification.setStatus(NotificationStatus.DELIVERED);
                }
            } catch (Exception e) {

                log.error("Error sending notification to user {}: {}", user.getEmail(), e.getMessage());
                handleNotificationSendingFailure(notification);
            } finally {

                notificationRepository.save(notification);
                handleNotificationHistory(notification);
            }
        }

        log.warn("No users found in the group with ID: {}", groupId);
        return mapper.mapToResponse(notification);
    }





    private void sendNotificationAndUpdateStatus(Notification notification, UserResponse user, String content) {
        try {

            //Twilio
//            smsSender.sendSms(content, user);

            simulateSmsSending(content, user);

            notification.setStatus(NotificationStatus.DELIVERED);
        } catch (NotificationSendException e) {
            throw new NotificationSendException("Failed to send SMS: " + e.getMessage());
        }
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
        NotificationHistory history = mapper.mapToHistory(notification);
        try {
            notificationHistoryRepository.save(history);

            if (notification.getStatus() == NotificationStatus.DELIVERED || notification.getStatus() == NotificationStatus.FAILED) {
                notificationRepository.delete(notification);
            }
        } catch (Exception e) {
            log.error("Error saving notification history or deleting notification: {}", e.getMessage());
        }
    }


    private void simulateSmsSending(String message, UserResponse user) {
        System.out.println("Sending SMS to: " + user.getPhone() + " with message: " + message);
        if (Math.random() > 0.7) {
            throw new NotificationSendException("Failed to send SMS");
        }
    }
}




