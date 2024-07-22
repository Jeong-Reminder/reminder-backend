package com.example.backend.model.repository.notification;

import com.example.backend.model.entity.notification.NotificationMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationMessageRepository extends CrudRepository<NotificationMessage, String> {
}