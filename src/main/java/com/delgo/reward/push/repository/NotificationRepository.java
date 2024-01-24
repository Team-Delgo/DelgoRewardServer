package com.delgo.reward.push.repository;

import com.delgo.reward.push.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findListByUserIdOrderByCreateAtDesc(int userId);
    Boolean existsByUserIdAndIsRead(int userId, boolean isRead);
}
