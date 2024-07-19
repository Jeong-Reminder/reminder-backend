package com.example.backend.service.notification;

import com.example.backend.dto.notification.NotificationResponseDTO;
import com.example.backend.model.entity.notification.NotificationMessage;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface NotificationService {
    void addMessage(String studentId, String messageId);
    void addMessageAllStudent(NotificationMessage message);
    List<NotificationResponseDTO> getMessagesByStudentId(Authentication authentication);
    void addMessageToStudent(NotificationMessage message, String studentId);
    void isReadMessage(Authentication authentication, String messageId);
    void cleanUpExpiredMessages();
}
