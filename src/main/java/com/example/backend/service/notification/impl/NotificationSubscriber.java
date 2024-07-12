package com.example.backend.service.notification.impl;

import org.springframework.stereotype.Service;

@Service
public class NotificationSubscriber {

    public void onMessage(String message) {
        // 메시지 처리 로직 구현
        System.out.println("Received message: " + message);
    }
}
