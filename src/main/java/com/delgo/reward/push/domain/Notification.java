package com.delgo.reward.push.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationId;
    private int userId;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private int objectId; // notifyType: {Mungple = mungpleId}, {Comment, Cute, Helper = certificationId}
    private Boolean isRead;

    private LocalDateTime createAt;

    public static Notification from(int userId, String notifyMsg, NotificationType notificationType, int objectId, LocalDateTime createAt) {
        return Notification.builder()
                .userId(userId)
                .message(notifyMsg)
                .notificationType(notificationType)
                .objectId(objectId)
                .isRead(false)
                .createAt(createAt)
                .build();
    }

    public Notification read() {
        return Notification.builder()
                .notificationId(notificationId)
                .userId(userId)
                .message(message)
                .notificationType(notificationType)
                .objectId(objectId)
                .isRead(true)
                .createAt(createAt)
                .build();
    }
}
