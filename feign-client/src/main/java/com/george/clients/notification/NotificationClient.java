package com.george.clients.notification;



import com.george.core.NotificationKafka;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "NOTIFICATION")
public interface NotificationClient {

    @GetMapping("/api/v1/notifications/")
    ResponseEntity<List<NotificationKafka>> getNotificationsForRebalancing(
            @RequestParam(name = "pending", required = false, defaultValue = "10") Long pendingSec,
            @RequestParam(name = "new", required = false, defaultValue = "10") Long newSec,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size
    );


    @PostMapping(value = "/api/v1/notifications/{notificationId}/sent")
    ResponseEntity<NotificationKafka> setNotificationAsSent(
            @RequestHeader Long userId,
            @PathVariable("notificationId") Long notificationId
    );

    @PostMapping(value = "/api/v1/notifications/{notificationId}/resending")
    ResponseEntity<NotificationKafka> setNotificationAsResending(
            @RequestHeader Long userId,
            @PathVariable("notificationId") Long notificationId
    );

    @PostMapping(value = "/api/v1/notifications/{notificationId}/corrupt")
    ResponseEntity<NotificationKafka> setNotificationAsCorrupt(
            @RequestHeader Long userId,
            @PathVariable("notificationId") Long notificationId
    );

    @PostMapping(value = "/api/v1/notifications/{notificationId}/error")
    ResponseEntity<NotificationKafka> setNotificationAsError(
            @RequestHeader Long userId,
            @PathVariable("notificationId") Long notificationId
    );
}
