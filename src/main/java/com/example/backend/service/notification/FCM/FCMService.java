package com.example.backend.service.notification.FCM;

import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.entity.notification.NotificationMessage;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.service.notification.rabbitMQ.RabbitMQSender;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMService {

    private final MemberRepository memberRepository;
    private final RabbitMQSender rabbitMQSender;

    public void sendMessage(String targetToken, NotificationMessage notificationMessage) {
        // RabbitMQSender에 전송할 메시지 객체 생성 및 전송
        rabbitMQSender.send(targetToken, notificationMessage);
    }

    public void sendMessageAllStudent(NotificationMessage notificationMessage) {
        List<Member> members = memberRepository.findByUserRole(UserRole.ROLE_USER);
        for (Member member : members) {
            sendMessage(member.getFcmToken(), notificationMessage);
        }
    }

    public void sendMessageToStudent(Member member, NotificationMessage message) {
        sendMessage(member.getFcmToken(), message);
    }
}
