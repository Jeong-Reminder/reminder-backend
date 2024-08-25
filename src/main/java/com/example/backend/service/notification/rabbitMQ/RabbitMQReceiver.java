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
        if (wrapper.getTargetToken() != null && !wrapper.getTargetToken().isEmpty()) {
            sendFCMMessage(wrapper.getTargetToken(), wrapper.getNotificationMessage());
        } else {
            System.err.println("Invalid target token: " + wrapper.getTargetToken());
        }
    }

    private void sendFCMMessage(String targetToken, NotificationMessage notificationMessage) {
        try {
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

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (IllegalArgumentException e) {
            System.err.println("Error sending Firebase message: Invalid argument - " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}