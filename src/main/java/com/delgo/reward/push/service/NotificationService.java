package com.delgo.reward.push.service;

import com.delgo.reward.push.domain.Notification;
import com.delgo.reward.push.repository.NotificationRepository;
import com.delgo.reward.push.domain.NotificationType;
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
    public Notification create(int userId, String notifyMsg, int objectId, NotificationType notificationType) {
        return notificationRepository.save(Notification.from(userId, notifyMsg, notificationType, objectId, LocalDateTime.now()));
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
