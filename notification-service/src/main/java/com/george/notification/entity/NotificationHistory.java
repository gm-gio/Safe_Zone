package com.george.notification.entity;


import com.george.notification.enums.NotificationStatus;
import com.george.notification.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications_history")
@FieldDefaults(level = PRIVATE)
public class NotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private Long userId;
    private Long templateId;
    private Long groupId;


    @Enumerated(EnumType.STRING)
    NotificationType type;

    @Enumerated(EnumType.STRING)
    NotificationStatus status;


    Integer retryAttempts;

    LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        NotificationHistory history= (NotificationHistory) o;

        return id != null && id.equals(history.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
