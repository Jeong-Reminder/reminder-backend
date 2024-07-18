package com.example.backend.model.entity.notification;

import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "NotificationMessage",timeToLive = 60 * 60 * 24 * 7)
public class NotificationMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String title;
    private String content;
    private boolean isRead;
    private String category;
    private Long targetId;
    private LocalDateTime createdAt;
}