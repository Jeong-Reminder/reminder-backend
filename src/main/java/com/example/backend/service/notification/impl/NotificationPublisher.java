package com.example.backend.service.notification.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    // 알림 메시지를 Redis 채널에 발행하는 메서드
    public void publish(Object message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
