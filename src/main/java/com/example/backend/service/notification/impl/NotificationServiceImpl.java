package com.example.backend.service.notification.impl;

import com.example.backend.dto.notification.NotificationRequestDTO;
import com.example.backend.dto.notification.NotificationResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.entity.notification.Notification;
import com.example.backend.model.repository.announcement.AnnouncementRepository; // 추가
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.notification.NotificationRepository;
import com.example.backend.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberRepository memberRepository;
    private final AnnouncementRepository announcementRepository;
    private final ChannelTopic topic;

    @Override
    public NotificationResponseDTO createNotification(Authentication authentication, NotificationRequestDTO requestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));

        Announcement announcement = announcementRepository.findById(requestDTO.getAnnouncementId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다: " + requestDTO.getAnnouncementId()));

        Notification notification = Notification.builder()
                .category(requestDTO.getCategory())
                .title(requestDTO.getTitle())
                .message(requestDTO.getMessage())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .member(member)
                .announcement(announcement)
                .build();
        notificationRepository.save(notification);

        redisTemplate.convertAndSend(topic.getTopic(),
                new NotificationResponseDTO(
                        notification.getId(),
                        notification.getCategory(),
                        notification.getTitle(),
                        notification.getMessage(),
                        notification.isRead()
                ));

        return new NotificationResponseDTO(
                notification.getId(),
                notification.getCategory(),
                notification.getTitle(),
                notification.getMessage(),
                notification.isRead());
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsForMember(Authentication authentication) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));

        List<Notification> notifications = notificationRepository.findAllByIsReadFalse();
        return notifications.stream().map(notification ->
                new NotificationResponseDTO(
                        notification.getId(),
                        notification.getCategory(),
                        notification.getTitle(),
                        notification.getMessage(),
                        notification.isRead()

                )).collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Authentication authentication, Long notificationId) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));

        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 보이게 할 수 있습니다.");
        }
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
