package com.delgo.reward.notification.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTest {

    @Test
    void from() {
        // given
        int userId = 1;
        String message = "test msg";
        int certificationId = 12;
        NotificationType notificationType = NotificationType.Comment;
        LocalDateTime createAt = LocalDateTime.now();

        // when
        Notification notification = Notification.from(userId, message, notificationType, certificationId, createAt);

        // then
        assertThat(notification.getUserId()).isEqualTo(userId);
        assertThat(notification.getMessage()).isEqualTo(message);
        assertThat(notification.getNotificationType()).isEqualTo(notificationType);
        assertThat(notification.getObjectId()).isEqualTo(certificationId);
        assertThat(notification.getIsRead()).isEqualTo(false);
        assertThat(notification.getCreateAt()).isEqualTo(createAt);
    }

    @Test
    void read() {
        // given
        Notification notification = Notification.builder()
                .userId(1)
                .message("test message")
                .notificationType(NotificationType.Comment)
                .objectId(12)
                .isRead(false)
                .createAt(LocalDateTime.now())
                .build();

        // when
        Notification updatedNotification = notification.read();

        // then
        assertThat(updatedNotification.getIsRead()).isEqualTo(true);
    }

    @Test
    void NoArgsConstructor(){
        // given

        // when
        Notification notification = new Notification();

        // then
        assertThat(notification).isNotNull();
    }
}