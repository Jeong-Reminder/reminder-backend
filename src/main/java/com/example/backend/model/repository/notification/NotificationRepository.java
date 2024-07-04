package com.example.backend.model.repository.notification;

import com.example.backend.model.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByIsReadFalse();
}
