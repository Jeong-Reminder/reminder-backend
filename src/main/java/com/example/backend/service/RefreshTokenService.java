package com.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate stringRedisTemplate;

    public void saveRefreshToken(String studentId, String refreshToken, long duration) {
        stringRedisTemplate.opsForValue().set(studentId, refreshToken, duration, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String studentId) {
        return stringRedisTemplate.opsForValue().get(studentId);
    }

    public void deleteRefreshToken(String studentId) {
        stringRedisTemplate.delete(studentId);
    }
}
