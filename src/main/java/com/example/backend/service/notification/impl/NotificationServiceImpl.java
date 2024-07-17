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

}
