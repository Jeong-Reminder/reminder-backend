package com.example.backend.controller.notification;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.notification.NotificationResponseDTO;
import com.example.backend.model.entity.notification.NotificationMessage;
import com.example.backend.service.notification.FCM.FCMService;
import com.example.backend.service.notification.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final FCMService fcmService;
    private final NotificationService notificationService;

    @PostMapping("/test-send")
    public String sendNotification(@RequestParam String targetToken, @RequestBody NotificationMessage notificationMessage) {
        fcmService.sendMessage(targetToken, notificationMessage);
        return "Notification sent successfully!";
    }

    @GetMapping
    public ResponseDTO<Object> getNotificationsByMember(Authentication authentication) {
        List<NotificationResponseDTO> notificationResponseDTOList = notificationService.getMessagesByStudentId(authentication);

        return ResponseDTO.builder()
                .status(200)
                .data(notificationResponseDTOList)
                .build();
    }

    @PutMapping("{messageId}")
    public ResponseDTO<Object> isReadNotification(Authentication authentication, 
                                                  @PathVariable String messageId) {
        notificationService.isReadMessage(authentication, messageId);
        return ResponseDTO.builder()
                .status(200)
                .data(null)
                .build();
    }
}
