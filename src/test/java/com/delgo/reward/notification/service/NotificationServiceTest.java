package com.delgo.reward.notification.service;

import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.notification.domain.Notification;
import com.delgo.reward.notification.domain.NotificationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class NotificationServiceTest {
    @Autowired
    NotificationService notificationService;

    @Test
    @Transactional
    void create() {
        // given
        int userId = 1;
        int certificationId = 12;
        String image = "test image";
        String profile = "test profile";
        String notifyMsg = "test msg";
        NotificationType notificationType = NotificationType.Comment;

        // when
        Notification notification = notificationService.create(userId, image, profile, notifyMsg, certificationId, notificationType);

        // then
        assertThat(notification.getUserId()).isEqualTo(userId);
        assertThat(notification.getImage()).isEqualTo(image);
        assertThat(notification.getProfile()).isEqualTo(profile);
        assertThat(notification.getMessage()).isEqualTo(notifyMsg);
        assertThat(notification.getNotificationType()).isEqualTo(notificationType);
    }

    @Test
    @Transactional
    void createByMungple() {
        // given
        int userId = 1;
        String image = "test image";
        String notifyMsg = "test msg";
        NotificationType notificationType = NotificationType.Comment;
        Mungple mungple = Mungple.builder()
                .mungpleId(1)
                .photoUrls(List.of(image))
                .build();

        // when
        Notification notification = notificationService.createByMungple(mungple, userId, notifyMsg, notificationType);

        // then
        assertThat(notification.getUserId()).isEqualTo(userId);
        assertThat(notification.getImage()).isEqualTo(image);
        assertThat(notification.getMessage()).isEqualTo(notifyMsg);
        assertThat(notification.getObjectId()).isEqualTo(mungple.getMungpleId());
        assertThat(notification.getNotificationType()).isEqualTo(notificationType);
    }

    @Test
    @Transactional
    void read() {
        // given
        List<Notification> notificationList = List.of(Notification.builder().isRead(false).build());

        // when
        List<Notification> updatedList = notificationService.read(notificationList);

        // then
        assertThat(updatedList.get(0).getIsRead()).isTrue();
    }

    @Test
    void getListByUserId() {
        // given
        int userId = 276;
        LocalDateTime startDate = LocalDateTime.now().minusDays(15);
        LocalDateTime endDate = LocalDateTime.now();

        // when
        List<Notification> notificationList = notificationService.getListByUserIdAndDate(userId, startDate, endDate);

        // then
        assertThat(notificationList.size()).isGreaterThan(0);
        assertThat(notificationList.size()).isEqualTo(3);
        assertThat(notificationList).extracting(Notification::getUserId).containsOnly(userId);
        assertThat(notificationList)
                .extracting(Notification::getCreateAt)
                .allMatch(date ->
                                !date.isBefore(startDate) && !date.isAfter(endDate),
                        "All notifications should be within the date range"
                );
    }

    @Test
    void hasUnreadNotification() {
        // given
        int userId = 276;

        // when
        boolean result = notificationService.hasUnreadNotification(userId);

        // then
        assertThat(result).isTrue();
    }
}