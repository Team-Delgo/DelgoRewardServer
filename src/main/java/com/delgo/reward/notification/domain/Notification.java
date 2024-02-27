package com.delgo.reward.notification.domain;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.mungple.domain.Mungple;
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
    private String image;
    private String profile;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private int objectId; // notifyType: {Mungple = mungpleId}, {Comment, Cute, Helper = certificationId}
    private Boolean isRead;
    @Enumerated(EnumType.STRING)
    private CategoryCode categoryCode; //  Mungple 관련 Notification 일 때만 존재

    private LocalDateTime createAt;

    public static Notification from(int userId, String image, String profile, String notifyMsg, NotificationType notificationType, int objectId, LocalDateTime createAt) {
        return Notification.builder()
                .userId(userId)
                .image(image)
                .profile(profile)
                .message(notifyMsg)
                .notificationType(notificationType)
                .objectId(objectId)
                .isRead(false)
                .createAt(createAt)
                .build();
    }

    public static Notification fromMungple(Mungple mungple, int userId, String notifyMsg, NotificationType notificationType, LocalDateTime createAt) {
        return Notification.builder()
                .userId(userId)
                .image(mungple.getThumbnailUrl())
                .message(notifyMsg)
                .notificationType(notificationType)
                .objectId(mungple.getMungpleId())
                .isRead(false)
                .categoryCode(mungple.getCategoryCode())
                .createAt(createAt)
                .build();
    }

    public Notification read() {
        this.isRead = true;
        return this;
    }
}
