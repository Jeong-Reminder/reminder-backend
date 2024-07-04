package com.example.backend.controller.notification;

import com.example.backend.dto.notification.NotificationRequestDTO;
import com.example.backend.dto.notification.NotificationResponseDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ResponseDTO<NotificationResponseDTO>> createNotification(Authentication authentication,
                                                                                   @RequestBody NotificationRequestDTO requestDTO) {
        NotificationResponseDTO responseDTO = notificationService.createNotification(authentication, requestDTO);
        ResponseDTO<NotificationResponseDTO> response = ResponseDTO.<NotificationResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .data(responseDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<NotificationResponseDTO>>> getNotificationsForMember(Authentication authentication) {
        List<NotificationResponseDTO> responseDTOs = notificationService.getNotificationsForMember(authentication);
        ResponseDTO<List<NotificationResponseDTO>> response = ResponseDTO.<List<NotificationResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(responseDTOs)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{notificationId}/mark-as-read")
    public ResponseEntity<ResponseDTO<Void>> markNotificationAsRead(Authentication authentication,
                                                                    @PathVariable Long notificationId) {
        notificationService.markAsRead(authentication, notificationId);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
}
