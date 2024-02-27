package com.delgo.reward.notification.service;

import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.notification.domain.Notification;
import com.delgo.reward.notification.repository.NotificationRepository;
import com.delgo.reward.notification.domain.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification create(int userId, String image, String profile, String notifyMsg, int objectId, NotificationType notificationType) {
        return notificationRepository.save(Notification.from(userId, image, profile, notifyMsg, notificationType, objectId, LocalDateTime.now()));
    }

    @Transactional
    public Notification createByMungple(Mungple mungple, int userId, String notifyMsg, NotificationType notificationType) {
        return notificationRepository.save(Notification.fromMungple(mungple, userId, notifyMsg, notificationType, LocalDateTime.now()));
    }

    @Transactional
    public List<Notification> read(List<Notification> notificationList) {
        return notificationRepository.saveAll(notificationList.stream().map(Notification::read).toList());
    }

    public List<Notification> getListByUserIdAndDate(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        return notificationRepository.findListByUserId(userId, startDate, endDate);
    }

    public boolean hasUnreadNotification(int userId){
        return notificationRepository.existsByUserIdAndIsRead(userId, false);
    }
}
