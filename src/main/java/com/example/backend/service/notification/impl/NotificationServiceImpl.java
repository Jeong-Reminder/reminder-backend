package com.example.backend.service.notification.impl;

import com.example.backend.dto.notification.NotificationResponseDTO;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.entity.notification.Notification;
import com.example.backend.model.entity.notification.NotificationMessage;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.notification.NotificationMessageRepository;
import com.example.backend.model.repository.notification.NotificationRepository;
import com.example.backend.service.notification.NotificationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMessageRepository notificationMessageRepository;
    private final MemberRepository memberRepository;

    @Override
    public void addMessage(String studentId, String messageId) {

        Optional<Notification> optionalNotification = notificationRepository.findById(studentId);
        Notification notification;
        if (optionalNotification.isPresent()) {
            notification = optionalNotification.get();
        } else {
            notification = new Notification();
            notification.setStudentId(studentId);
            notification.setMessageIds(new ArrayList<>());
        }
        notification.getMessageIds().add(messageId);
        notificationRepository.save(notification);
    }

    @Override
    public void addMessageAllStudent(NotificationMessage message) {
        List<Member> members = memberRepository.findByUserRole(UserRole.ROLE_USER);

        for(Member member : members){
            message.setId(UUID.randomUUID().toString());
            String messageId = message.getId();
            notificationMessageRepository.save(message);
            addMessage(member.getStudentId(), messageId);
        }

        NotificationResponseDTO.toResponseDTO(message);
    }

    @Override
    public void addMessageToStudent(NotificationMessage message, String studentId) {
        String messageId = message.getId();
        notificationMessageRepository.save(message);
        addMessage(studentId, messageId);
        NotificationResponseDTO.toResponseDTO(message);
    }

    @Override
    public void isReadMessage(Authentication authentication, String messageId) {
        NotificationMessage message = notificationMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메시지가 없습니다."));

        message.setRead(true);

        notificationMessageRepository.save(message);
    }

    @Override
    public List<NotificationResponseDTO> getMessagesByStudentId(Authentication authentication) {
        String studentId = authentication.getName();
        Optional<Notification> optionalNotification = notificationRepository.findById(studentId);
        List<NotificationMessage> messages = new ArrayList<>();
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            for (String messageId : notification.getMessageIds()) {
                Optional<NotificationMessage> message = notificationMessageRepository.findById(messageId);
                message.ifPresent(messages::add);
            }
        }
        return NotificationResponseDTO.toResponseDTOList(messages);
    }

    @Override
    @Scheduled(fixedRate = 86400000)
    public void cleanUpExpiredMessages() {
        List<Notification> notifications = (List<Notification>) notificationRepository.findAll();
        for (Notification notification : notifications) {
            List<String> messageIds = notification.getMessageIds();
            List<String> validMessageIds = new ArrayList<>();
            for (String messageId : messageIds) {
                if (notificationMessageRepository.existsById(messageId)) {
                    validMessageIds.add(messageId);
                }
            }
            notification.setMessageIds(validMessageIds);
            notificationRepository.save(notification);
        }
    }
}
