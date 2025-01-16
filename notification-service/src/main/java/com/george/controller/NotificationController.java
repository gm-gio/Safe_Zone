package com.george.controller;

import com.george.dto.NotificationRequest;
import com.george.dto.NotificationResponse;
import com.george.service.NotificationService;
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
    public ResponseEntity<String> sendSms(@PathVariable Long userId, @PathVariable Long notificationId) {
        NotificationResponse response = notificationService.sendNotification(userId, notificationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
