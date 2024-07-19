package com.example.backend.model.entity.notification;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("Notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String studentId;
    private List<String> messageIds;

}
