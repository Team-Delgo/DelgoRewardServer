package com.delgo.reward.push.repository;

import com.delgo.reward.push.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("select n from Notification n where n.userId = :userId and n.createAt between :startDate and :endDate order by n.createAt desc , n.notificationId desc")
    List<Notification> findListByUserId(@Param("userId") int userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    Boolean existsByUserIdAndIsRead(int userId, boolean isRead);
}
