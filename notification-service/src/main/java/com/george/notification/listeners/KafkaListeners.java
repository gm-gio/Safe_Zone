package com.george.notification.listeners;

import com.george.clients.template.TemplateResponse;
import com.george.clients.user.UserClient;
import com.george.clients.user.UserResponse;

import com.george.core.UserListKafka;
import com.george.notification.dto.NotificationKafka;
import com.george.notification.dto.NotificationRequest;
import com.george.notification.dto.NotificationResponse;
import com.george.notification.enums.NotificationType;
import com.george.notification.mapper.NotificationMapper;
import com.george.notification.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final KafkaTemplate<String, NotificationKafka> kafkaTemplate;
    private final NotificationService notificationService;
    private final UserClient userClient;
    private final NotificationMapper mapper;


    @Value("${spring.kafka.topics.email}")
    private String emailTopic;

    @Value("${spring.kafka.topics.phone}")
    private String phoneTopic;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    @KafkaListener(
            topics = "${spring.kafka.topics.router}",
            groupId = "emergency-notifications"
    )

    private void listener(UserListKafka userListKafka) {


        Runnable runnable = () -> {
            TemplateResponse templateResponse = userListKafka.getTemplateResponse();
            List<Long> userIds = userListKafka.getUserIds();

            for (Long userId : userIds) {
                UserResponse response;

                try {
                    response = userClient.getUserById(userId);

                } catch (RuntimeException e) {
                    // TODO
                    continue;
                }

                if (response == null) {
                    continue;
                }

                sendNotificationByCredential(response::getEmail, NotificationType.EMAIL, response, templateResponse, emailTopic);
                sendNotificationByCredential(response::getPhone, NotificationType.PHONE, response, templateResponse, phoneTopic);


            }

        };
        executorService.submit(runnable);
    }


    private void sendNotificationByCredential( // TODO: too many params
                                               Supplier<String> supplier,
                                               NotificationType type,
                                               UserResponse userResponse,
                                               TemplateResponse templateResponse,
                                               String topic
    ) {

        String credential = supplier.get();

        if (credential != null) {
            Long notificationId;
            try {
                notificationId = notificationService.createNotification( // TODO: mapper
                        NotificationRequest.builder()
                                .type(type)
                                .userId(userResponse.getUserId())
                                .templateId(templateResponse.getTemplateId())
                                .credential(credential)
                                .build()
                ).getNotificationId();
            } catch (EntityNotFoundException e) {
                // TODO
                return;
            }
            NotificationResponse response = notificationService.setNotificationAsPending(notificationId);
            NotificationKafka notificationKafka = NotificationKafka.builder()
                    .id(response.getNotificationId())
                    .type(response.getType())
                    .credential(response.getCredential())
                    .status(response.getStatus())
                    .retryAttempts(response.getRetryAttempts())
                    .userId(response.getUserId())
                    .build();

            kafkaTemplate.send(topic, notificationKafka);

        }


    }


}
