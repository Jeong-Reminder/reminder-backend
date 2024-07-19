package com.example.backend.model.repository.notification;

import com.example.backend.model.entity.notification.Notification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, String> {
}