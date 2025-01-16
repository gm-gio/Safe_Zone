package com.george.entity;


import com.george.enums.NotificationStatus;
import com.george.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.george.enums.NotificationStatus.NEW;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
@FieldDefaults(level = PRIVATE)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long notificationId;

    Long userId;

    String message;
    @Enumerated(EnumType.STRING)
    NotificationType type;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    NotificationStatus status = NEW;

    @Builder.Default
    Integer retryAttempts = 0;

    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Notification notification = (Notification) o;

        return notificationId != null && notificationId.equals(notification.notificationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationId);
    }
}
