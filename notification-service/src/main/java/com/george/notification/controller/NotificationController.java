package com.george.notification.controller;

import com.george.notification.dto.NotificationRequest;
import com.george.notification.dto.NotificationResponse;
import com.george.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/new")
    @Operation(summary = "create a Notification")
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody NotificationRequest request) {
        return ResponseEntity.status(CREATED).body(notificationService.createNotification(request));
    }

    @PostMapping("/send/{userId}/{notificationId}")
    @Operation(summary = "send a Notification to User")
    public ResponseEntity<String> sendNotification(@PathVariable Long userId, @PathVariable Long notificationId) {
        NotificationResponse response = notificationService.sendNotification(userId, notificationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/sendToGroup/{groupId}/{notificationId}")
    @Operation(summary ="send a Notification to group" )
    public ResponseEntity<String> sendNotificationToGroup(
            @PathVariable Long groupId,
            @PathVariable Long notificationId) {
        NotificationResponse response = notificationService.sendNotificationToGroup(groupId, notificationId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
