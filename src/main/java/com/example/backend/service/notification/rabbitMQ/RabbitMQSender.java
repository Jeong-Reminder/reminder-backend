package com.example.backend.service.notification.rabbitMQ;

import com.example.backend.model.entity.notification.NotificationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public RabbitMQSender(RabbitTemplate rabbitTemplate,
                          @Value("${rabbitmq.exchange.name}") String exchangeName,
                          @Value("${rabbitmq.routing.key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    public void send(String targetToken, NotificationMessage notificationMessage) {
        // targetToken을 포함한 메시지 객체를 전송
        rabbitTemplate.convertAndSend(exchangeName, routingKey, new NotificationMessageWrapper(targetToken, notificationMessage));
    }

    // 내부 클래스 또는 별도의 파일로 NotificationMessageWrapper 클래스 정의
    public static class NotificationMessageWrapper {
        private String targetToken;
        private NotificationMessage notificationMessage;

        public NotificationMessageWrapper(String targetToken, NotificationMessage notificationMessage) {
            this.targetToken = targetToken;
            this.notificationMessage = notificationMessage;
        }

        public String getTargetToken() {
            return targetToken;
        }

        public void setTargetToken(String targetToken) {
            this.targetToken = targetToken;
        }

        public NotificationMessage getNotificationMessage() {
            return notificationMessage;
        }

        public void setNotificationMessage(NotificationMessage notificationMessage) {
            this.notificationMessage = notificationMessage;
        }
    }

}