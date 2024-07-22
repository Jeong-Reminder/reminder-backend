package com.example.backend.service.notification.rabbitMQ;

import com.example.backend.model.entity.notification.NotificationMessage;
import com.example.backend.service.notification.rabbitMQ.RabbitMQSender.NotificationMessageWrapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQReceiver {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(NotificationMessageWrapper wrapper) {
        sendFCMMessage(wrapper.getTargetToken(), wrapper.getNotificationMessage());
    }

    private void sendFCMMessage(String targetToken, NotificationMessage notificationMessage) {
        Notification notification = Notification.builder()
                .setTitle(notificationMessage.getTitle())
                .setBody(notificationMessage.getContent())
                .setImage(null)
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .putData("id", notificationMessage.getId())
                .putData("title", notificationMessage.getTitle())
                .putData("content", notificationMessage.getContent())
                .putData("category", notificationMessage.getCategory())
                .putData("targetId", String.valueOf(notificationMessage.getTargetId()))
                .putData("createdAt", notificationMessage.getCreatedAt().toString())
                .putData("isRead", String.valueOf(notificationMessage.isRead()))
                .setToken(targetToken)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}