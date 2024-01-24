package com.delgo.reward.push.service;

import com.delgo.reward.push.domain.Notification;
import com.delgo.reward.push.repository.NotificationRepository;
import com.delgo.reward.push.domain.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public Notification create(int userId, String notifyMsg, int objectId, NotificationType notificationType) {
        return notificationRepository.save(Notification.from(userId, notifyMsg, notificationType, objectId, LocalDateTime.now()));
    }

    public List<Notification> getListByUserId(int userId) {
        return notificationRepository.findListByUserIdOrderByCreateAtDesc(userId);
    }

    public void read(List<Notification> notificationList) {
        notificationRepository.saveAll(notificationList.stream().map(Notification::read).toList());
    }

    public boolean hasUnreadNotification(int userId){
        return notificationRepository.existsByUserIdAndIsRead(userId, false);
    }
}
