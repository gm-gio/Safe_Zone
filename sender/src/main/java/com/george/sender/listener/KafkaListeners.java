package com.george.sender.listener;

import com.george.clients.notification.NotificationClient;
import com.george.clients.template.TemplateClient;
import com.george.clients.template.TemplateResponse;
import com.george.core.NotificationKafka;
import com.george.sender.config.twilio.SmsSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final NotificationClient notificationClient;
    private final TemplateClient templateClient;
    private final SmsSender smsSender;
    private final JavaMailSender javaMailSender;

    @Value("${notification.maxRetryAttempts}")
    private int maxRetryAttempts;

    @Value("${notification.mail.from}")
    private String mailFrom;

    @KafkaListener(topics = "${spring.kafka.topics.email}", groupId = "emergency", containerFactory = "kafkaListenerContainerFactory")
    public void emailNotificationListener(NotificationKafka notification) {

        log(notification);

        if (notification.getId() == null || notification.getUserId() == null) {
            log.error("Notification without id/userId, skipping: {}", notification);
            return;
        }

        try {
            sendEmail(notification);
        } catch (Exception e) {
            log.error("Failed to send email to {}", notification.getCredential(), e);
            handleSendingFailure(notification);
            return;
        }

        markAsSent(notification);
    }

    @KafkaListener(topics = "${spring.kafka.topics.phone}", groupId = "emergency", containerFactory = "kafkaListenerContainerFactory")
    public void phoneNotificationListener(NotificationKafka notification) {

        log(notification);

        if (notification.getId() == null || notification.getUserId() == null) {
            log.error("Notification without id/userId, skipping: {}", notification);
            return;
        }

        try {
            sendSms(notification);
        } catch (Exception e) {
            log.error("Failed to send SMS to {}", notification.getCredential(), e);
            handleSendingFailure(notification);
            return;
        }

        markAsSent(notification);
    }


    private void sendEmail(NotificationKafka notification) {

        TemplateResponse template =
                templateClient.getTemplateById(notification.getTemplateId());

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailFrom);
        message.setTo(notification.getCredential().trim());
        message.setSubject(template.getTitle());
        message.setText(template.getContent());

        javaMailSender.send(message);
    }


    private void sendSms(NotificationKafka notification) {

        TemplateResponse template =
                templateClient.getTemplateById(notification.getTemplateId());

        smsSender.sendSms(
                template.getContent(),
                notification.getCredential()
        );
    }

    private void sendSmsSimulate(NotificationKafka notification) {

        TemplateResponse template =
                templateClient.getTemplateById(notification.getTemplateId());

        if (Math.random() < 0.3) {
            throw new RuntimeException("Simulated SMS failure");
        }

        log.info("[FAKE SMS] to={}, text={}", notification.getCredential(), template.getContent());
    }



    private void markAsSent(NotificationKafka notification) {
        try {
            notificationClient.setNotificationAsSent(notification.getUserId(), notification.getId());
        } catch (feign.FeignException.NotFound e) {
            log.warn("Notification {} already processed, skipping", notification.getId());
        } catch (Exception e) {
            log.error("Failed to mark notification {} as sent", notification.getId(), e);
        }
    }

    private void handleSendingFailure(NotificationKafka notification) {
        try {
            if (notification.getRetryAttempts() >= maxRetryAttempts) {
                notificationClient.setNotificationAsError(notification.getUserId(), notification.getId());
            } else {
                notificationClient.setNotificationAsResending(notification.getUserId(), notification.getId());
            }
        } catch (feign.FeignException.NotFound e) {
            log.warn("Notification {} not found, skipping", notification.getId());
        } catch (Exception e) {
            log.error("Failed to update status for notification {}", notification.getId(), e);
        }
    }



    private void log(NotificationKafka notification) {

        log.info(
                "Sending {} notification to `{}`, id={}, userId={}, templateId={}, status={}, retryAttempts={}",
                notification.getType(),
                notification.getCredential(),
                notification.getId(),
                notification.getUserId(),
                notification.getTemplateId(),
                notification.getStatus(),
                notification.getRetryAttempts()
        );
    }
}