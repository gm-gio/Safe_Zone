package com.george.notification.controller;

import com.george.core.NotificationKafka;
import com.george.notification.dto.request.NotificationRequest;
import com.george.notification.dto.response.NotificationHistoryResponse;
import com.george.notification.dto.response.NotificationResponse;
import com.george.notification.service.impl.NotificationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static org.springframework.http.HttpStatus.OK;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationServiceImpl notificationServiceImpl;


    @GetMapping("/")
    @Operation(summary = "FOR REBALANCER: get Resending/Pending/New Notifications (set Pending status)")
    public ResponseEntity<List<NotificationKafka>> getNotificationsForRebalancing(
            @RequestParam(name = "pending", required = false, defaultValue = "10") Long pendingSec,
            @RequestParam(name = "new", required = false, defaultValue = "10") Long newSec,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size
    ) {
        return ResponseEntity.status(OK).body(
                notificationServiceImpl.getNotificationsForRebalancing(pendingSec, newSec, size)
        );
    }



    @Operation(
            summary = "Send notifications",
            description = "Creates notifications for multiple users based on the given template and sends them to the Kafka queue"
    )
    @PostMapping("/send/{templateId}")
    public ResponseEntity<String> sendNotifications(
            @Parameter(description = "ID of the notification template", required = true)
            @PathVariable Long templateId
    ) {
        String result = notificationServiceImpl.distributeNotifications(templateId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{notificationId}/sent")
    @Operation(summary = "set Notification status as successfully sent to Recipient")
    public ResponseEntity<NotificationHistoryResponse> setNotificationAsASent(
            @RequestHeader Long userId,
            @PathVariable("notificationId") Long notificationId
    ) {
        return ResponseEntity.status(OK).body(notificationServiceImpl.setNotificationAsASent(userId, notificationId));
    }

    @PostMapping("/{notificationId}/error")
    @Operation(summary = "set Notification status as error")
    public ResponseEntity<NotificationHistoryResponse> setNotificationAsError(
            @RequestHeader Long userId,
            @PathVariable("notificationId") Long notificationId
    ) {
        return ResponseEntity.status(OK).body(notificationServiceImpl.setNotificationAsFailed(userId, notificationId));
    }

    @PostMapping("/{notificationId}/corrupt")
    @Operation(summary = "set Notification status as impossible to sent")
    public ResponseEntity<NotificationHistoryResponse> setNotificationAsCorrupt(
            @RequestHeader Long userId,
            @PathVariable("notificationId") Long notificationId
    ) {
        return ResponseEntity.status(OK).body(notificationServiceImpl.setNotificationAsCorrupt(userId, notificationId));
    }

    @PostMapping("/{notificationId}/resending")
    @Operation(summary = "set Notification status as waiting to be resend")
    public ResponseEntity<NotificationResponse> setNotificationAsResending(
            @RequestHeader Long userId,
            @PathVariable("notificationId") Long notificationId
    ) {
        return ResponseEntity.status(OK).body(notificationServiceImpl.setNotificationAsResending(userId, notificationId));
    }



}
