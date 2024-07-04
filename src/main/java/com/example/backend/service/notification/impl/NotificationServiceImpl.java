package com.example.backend.service.notification.impl;

import com.example.backend.dto.notification.NotificationRequestDTO;
import com.example.backend.dto.notification.NotificationResponseDTO;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.notification.Notification;

import com.example.backend.model.repository.notification.NotificationRepository;
import com.example.backend.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;

    @Override
    public NotificationResponseDTO createNotification(Authentication authentication, NotificationRequestDTO requestDTO) {
        Member member = (Member) authentication.getPrincipal();
        Notification notification = Notification.builder()
                .category(requestDTO.getCategory())
                .title(requestDTO.getTitle())
                .message(requestDTO.getMessage())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .member(member)
                .build();
        notificationRepository.save(notification);
        return new NotificationResponseDTO(notification.getId(), notification.getCategory(), notification.getTitle(),
                notification.getMessage(), notification.isRead(), notification.getCreatedAt());
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsForMember(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        List<Notification> notifications = notificationRepository.findAllByIsReadFalse();
        return notifications.stream().map(notification -> new NotificationResponseDTO(notification.getId(),
                notification.getCategory(), notification.getTitle(), notification.getMessage(), notification.isRead(),
                notification.getCreatedAt())).collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Authentication authentication, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
