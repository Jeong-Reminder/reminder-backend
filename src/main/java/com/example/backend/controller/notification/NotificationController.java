package com.example.backend.controller;

import com.example.backend.dto.notification.NotificationRequestDTO;
import com.example.backend.dto.notification.NotificationResponseDTO;
import com.example.backend.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class NotificationController {

    private final NotificationService notificationService;

    // WebSocket을 통해 클라이언트가 "/app/notifications"로 메시지를 전송하면 이 메서드가 호출됨
    @MessageMapping("/notifications")
    @SendTo("/topic/notifications")
    public NotificationResponseDTO sendNotification(NotificationRequestDTO notificationRequestDTO, Authentication authentication) {
        // 알림 생성 및 발행 로직
        return notificationService.createNotification(authentication, notificationRequestDTO);
    }

    // 특정 회원의 모든 읽지 않은 알림을 가져오는 엔드포인트
    @GetMapping("/notifications")
    public List<NotificationResponseDTO> getNotifications(Authentication authentication) {
        return notificationService.getNotificationsForMember(authentication);
    }

    // 특정 알림을 읽음 상태로 변경하는 엔드포인트
    @PutMapping("/notifications/{notificationId}/read")
    public void markAsRead(@PathVariable Long notificationId, Authentication authentication) {
        notificationService.markAsRead(authentication, notificationId);
    }

}
