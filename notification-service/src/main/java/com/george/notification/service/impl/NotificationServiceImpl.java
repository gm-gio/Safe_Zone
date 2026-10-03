package com.george.notification.service.impl;

import com.george.clients.template.TemplateClient;
import com.george.clients.template.TemplateResponse;
import com.george.clients.urlShortener.ShortenerClient;
import com.george.clients.user.UserClient;
import com.george.core.NotificationKafka;
import com.george.core.TemplateResponseForUserListK;
import com.george.core.UserListKafka;
import com.george.notification.dto.request.NotificationRequest;
import com.george.notification.dto.response.NotificationHistoryResponse;
import com.george.notification.dto.response.NotificationResponse;
import com.george.notification.entity.Notification;
import com.george.notification.enums.NotificationStatus;
import com.george.notification.exception.NotificationCreationException;
import com.george.notification.exception.NotificationNotFoundException;
import com.george.notification.exception.UsersNotFoundException;
import com.george.notification.mapper.NotificationMapper;
import com.george.notification.repository.NotificationHistoryRepository;
import com.george.notification.repository.NotificationRepository;
import com.george.notification.service.NotificationHistoryService;
import com.george.notification.service.NotificationService;
import com.george.notification.util.ListSplitter;
import com.george.notification.util.NodeTracker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.george.notification.enums.NotificationStatus.RESENDING;
import static java.time.temporal.ChronoUnit.SECONDS;


@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService, NotificationHistoryService {

    private final NotificationRepository notificationRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;
    private final NotificationMapper mapper;

    private final UserClient userClient;
    private final TemplateClient templateClient;
    private final NodeTracker nodeTracker;
    private final KafkaTemplate<String, UserListKafka> kafkaTemplate;
    private final ShortenerClient shortenerClient;




    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.kafka.topics.router}")
    private String userListRoutingTopic;

    @Override
    public String distributeNotifications(Long templateId) {
        List<Long> userIds = userClient.getUserIds();

        if (userIds == null || userIds.isEmpty()) {
            throw new UsersNotFoundException("users.not_found");
        }

        TemplateResponse templateResponse =
                templateClient.getTemplateById(templateId);

        if (templateResponse == null) {
            throw new NotificationNotFoundException("Template not found");
        }

        TemplateResponseForUserListK kafkaTemplateResponse =
                TemplateResponseForUserListK.builder()
                        .templateId(templateResponse.getTemplateId())
                        .title(templateResponse.getTitle())
                        .content(templateResponse.getContent())
                        .build();

        for (List<Long> batch : splitUsers(userIds)) {
            UserListKafka listKafka =
                    new UserListKafka(kafkaTemplateResponse, batch);

            kafkaTemplate.send(userListRoutingTopic, listKafka);
        }

        return "Notification sent successfully";
    }



    @Override
    public NotificationResponse createNotification(NotificationRequest request) {

        return Optional.of(request)
                .map(mapper::mapToEntity)
                .map(notification -> notification.addTemplateId(request.getTemplateId()))
                .map(notificationRepository::saveAndFlush)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new NotificationCreationException("Failed to create notification"));
    }

    @Override
    public NotificationHistoryResponse setNotificationAsASent(Long userId, Long notificationId) {
        return setNotificationAsExecutedWithGivenStatus(
                userId,
                notificationId,
                NotificationStatus.DELIVERED
        );
    }

    @Override
    public NotificationHistoryResponse setNotificationAsFailed(Long userId, Long notificationId) {
        return setNotificationAsExecutedWithGivenStatus(
                userId,
                notificationId,
                NotificationStatus.FAILED
        );
    }

    @Override
    public NotificationHistoryResponse setNotificationAsCorrupt(Long userId, Long notificationId) {
        return setNotificationAsExecutedWithGivenStatus(
                userId,
                notificationId,
                NotificationStatus.UNDELIVERABLE
        );
    }

    @Override
    public NotificationResponse setNotificationAsPending(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .map(n -> {
                    n.setStatus(NotificationStatus.IN_PROGRESS);
                    return n;
                })
                .map(notificationRepository::saveAndFlush)
                .map(mapper::mapToResponse)
                .orElseThrow(() ->
                        new NotificationNotFoundException("Notification not found: " + notificationId));
    }


    public NotificationResponse setNotificationAsResending(Long userId, Long notificationId) {
        return notificationRepository.findByNotificationIdAndUserId(notificationId, userId)
                .map(Notification::incrementRetryAttempts)
                .map(notification -> notification.setNotificationStatus(RESENDING))
                .map(notificationRepository::saveAndFlush)
                .map(mapper::mapToResponse)
                .orElseThrow(() ->   new NotificationNotFoundException("Notification not found: " + notificationId + userId));
    }

    @Override
    public List<NotificationKafka> getNotificationsForRebalancing(Long pendingSec, Long newSec, Integer size){
        LocalDateTime now = LocalDateTime.now();
        return notificationRepository.findNotificationsByStatusAndCreatedAt(
                        now.minus(pendingSec, SECONDS), now.minus(newSec, SECONDS), Pageable.ofSize(size)
                ).stream()
                .map(notification -> notification.setNotificationStatus(NotificationStatus.IN_PROGRESS))
                .map(Notification::updateCreatedAt)
                .map(notificationRepository::saveAndFlush)
                .map(notification -> mapper.mapToKafka(notification, templateClient, shortenerClient))
                .toList();
    }



    private NotificationHistoryResponse setNotificationAsExecutedWithGivenStatus(
            Long userId, Long notificationId,
            NotificationStatus status
    ) {
        return notificationRepository.findByNotificationIdAndUserId(notificationId, userId)
                .map(notification -> {
                    notificationRepository.delete(notification);
                    return notification;
                })
                .map(mapper::mapToHistory)
                .map(notificationHistory -> notificationHistory.setNotificationStatus(status))
                .map(notificationHistoryRepository::saveAndFlush)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new NotificationNotFoundException(
                        "Notification not found: " + notificationId + " for user: " + userId
                ));
    }



    private List<List<Long>> splitUsers(List<Long> list) {
        return ListSplitter.splitListIntoParts(list, nodeTracker.getActiveNodeCount(applicationName));
    }


}




