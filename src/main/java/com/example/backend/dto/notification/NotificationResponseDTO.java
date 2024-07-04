package com.example.backend.dto.notification;

import com.example.backend.model.entity.notification.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private String category;
    private String title;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;


    public static NotificationResponseDTO toResponseDTO(Notification notification) {
        NotificationResponseDTO responseDTO = new NotificationResponseDTO();
        responseDTO.setId(notification.getId());
        responseDTO.setCategory(notification.getCategory());
        responseDTO.setTitle(notification.getTitle());
        responseDTO.setMessage(notification.getMessage());
        responseDTO.setRead(notification.isRead());
        responseDTO.setCreatedAt(notification.getCreatedAt());
        return responseDTO;
    }
}
