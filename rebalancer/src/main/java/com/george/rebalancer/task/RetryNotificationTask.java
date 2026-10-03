package com.george.rebalancer.task;


import com.george.clients.notification.NotificationClient;
import com.george.core.NotificationKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;



@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class RetryNotificationTask {

    @Value("${spring.kafka.topics.email}")
    private String emailTopic;

    @Value("${spring.kafka.topics.phone}")
    private String phoneTopic;

    @Value("${rebalancer.seconds-before-resend-pending}")
    private Long secondsBeforeResendPending;

    @Value("${rebalancer.seconds-before-resend-new}")
    private Long secondsBeforeResendNew;

    @Value("${rebalancer.max-amount-to-fetch}")
    private Integer amountToFetch;

    private final NotificationClient notificationClient;
    private final KafkaTemplate<String, NotificationKafka> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    public void renotify() {

        List<NotificationKafka> notificationKafkaList;

        try {
            notificationKafkaList = notificationClient.getNotificationsForRebalancing(
                    secondsBeforeResendPending,
                    secondsBeforeResendNew,
                    amountToFetch
            ).getBody();
        } catch (Exception e) {
            log.warn("Notification service unavailable, skipping this run: {}", e.getMessage());
            return;
        }

        if (notificationKafkaList == null || notificationKafkaList.isEmpty()) {
            return;
        }

        log.info("Rebalancing {} notifications", notificationKafkaList.size());

        for (NotificationKafka notification : notificationKafkaList) {
            try {
                switch (notification.getType()) {
                    case PHONE -> kafkaTemplate.send(phoneTopic, notification);
                    case EMAIL -> kafkaTemplate.send(emailTopic, notification);
                }
            } catch (Exception e) {
                log.error("Failed to send notification id={} to Kafka", notification.getId(), e);
            }
        }
    }


}
