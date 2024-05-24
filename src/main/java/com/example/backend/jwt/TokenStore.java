package com.example.backend.jwt;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TokenStore {

    private final RedisTemplate<String, String> redisTemplate;
    private final ValueOperations<String, String> valueOperations;

    @Autowired
    public TokenStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void storeRefreshToken(String studentId, String refreshToken, long duration) {
        valueOperations.set(studentId, refreshToken, duration, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String studentId) {
        return valueOperations.get(studentId);
    }

    public void removeRefreshToken(String studentId) {
        redisTemplate.delete(studentId);
    }
}