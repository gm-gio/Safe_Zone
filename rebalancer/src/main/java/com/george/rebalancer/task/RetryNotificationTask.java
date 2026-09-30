package com.george.rebalancer.task;


import com.george.clients.notification.NotificationClient;
import com.george.core.NotificationKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;



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
    private void renotify() {
        List<NotificationKafka> notificationKafkaList = notificationClient.getNotificationsForRebalancing( // TODO: exception handling if service unavailable
                secondsBeforeResendPending,
                secondsBeforeResendNew,
                amountToFetch
        ).getBody();

        if (notificationKafkaList == null || notificationKafkaList.isEmpty()) {
            return;
        }

        for (NotificationKafka notification : notificationKafkaList) {
            switch (notification.getType()) {
                case PHONE -> kafkaTemplate.send(phoneTopic, notification);
                case EMAIL -> kafkaTemplate.send(emailTopic, notification);

            }
        }
    }


}
