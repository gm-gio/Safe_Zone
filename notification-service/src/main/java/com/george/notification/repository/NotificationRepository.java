package com.george.notification.repository;

import com.george.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByNotificationIdAndUserId(Long notificationId, Long useId);

    @Query("""
            SELECT n
            FROM Notification n
            WHERE
            n.status = 'RESENDING'
                OR
            n.status = 'NEW' AND n.createdAt < :newDateTime
                OR
            n.status = 'IN_PROGRESS' AND n.createdAt < :pendingDateTime
            """)
    List<Notification> findNotificationsByStatusAndCreatedAt(
            @Param("pendingDateTime") LocalDateTime pendingDateTime,
            @Param("newDateTime") LocalDateTime newDateTime,
            Pageable pageable
    );
}