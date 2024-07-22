package com.example.backend.model.entity.member;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "refresh", timeToLive = 60 * 60 * 24 * 10)
@Builder
public class Refresh {

    @Id
    private String id;
    private String refresh;
}
