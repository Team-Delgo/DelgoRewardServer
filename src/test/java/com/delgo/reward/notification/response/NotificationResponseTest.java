package com.delgo.reward.notification.response;

import com.delgo.reward.notification.domain.Notification;
import com.delgo.reward.notification.domain.NotificationType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationResponseTest {

    @Test
    void from() {
        // given
        Notification notification = Notification.builder()
                .notificationId(1)
                .userId(1)
                .message("test message")
                .notificationType(NotificationType.Comment)
                .objectId(23)
                .isRead(false)
                .createAt(LocalDateTime.now())
                .build();

        // when
        NotificationResponse response = NotificationResponse.from(notification);

        // then
        assertThat(response.getNotificationId()).isEqualTo(notification.getNotificationId());
        assertThat(response.getUserId()).isEqualTo(notification.getUserId());
        assertThat(response.getMessage()).isEqualTo(notification.getMessage());
        assertThat(response.getNotificationType()).isEqualTo(notification.getNotificationType());
        assertThat(response.getObjectId()).isEqualTo(notification.getObjectId());
        assertThat(response.getIsRead()).isEqualTo(notification.getIsRead());
        assertThat(response.getCreateAt()).isEqualTo(notification.getCreateAt());
    }

    @Test
    void fromList() {
        // given
        List<Notification> notificationList = List.of(
                Notification.builder().notificationId(1).build(),
                Notification.builder().notificationId(2).build()
        );

        // when
        List<NotificationResponse> responseList = NotificationResponse.fromList(notificationList);

        // then
        assertThat(responseList.size()).isEqualTo(2);
        assertThat(responseList.get(0).getNotificationId()).isEqualTo(notificationList.get(0).getNotificationId());
        assertThat(responseList.get(1).getNotificationId()).isEqualTo(notificationList.get(1).getNotificationId());
    }
}