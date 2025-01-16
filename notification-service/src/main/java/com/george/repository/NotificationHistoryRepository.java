package com.george.repository;

import com.george.entity.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {
}
