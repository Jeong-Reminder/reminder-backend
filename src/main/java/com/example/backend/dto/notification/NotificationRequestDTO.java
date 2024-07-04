package com.example.backend.dto.notification;


import com.example.backend.model.entity.member.Member;
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
public class NotificationRequestDTO {
    private String category;
    private String title;
    private String message;
    public Notification toEntity(Member member) {
        return Notification.builder()
                .category(category)
                .title(title)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .member(member)
                .build();
    }
}
