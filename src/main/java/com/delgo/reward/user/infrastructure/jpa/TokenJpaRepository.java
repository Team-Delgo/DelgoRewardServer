package com.delgo.reward.user.infrastructure.jpa;

import com.delgo.reward.user.infrastructure.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TokenJpaRepository extends JpaRepository<TokenEntity, Integer> {
    Optional<TokenEntity> findByUserId(int userId);
    List<TokenEntity> findAllByUserIdIn(List<Integer> userIdList);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE TokenEntity t SET t.fcmToken = :fcmToken WHERE t.userId = :userId")
    void updateFcmTokenByUserId(@Param("userId") int userId, @Param("fcmToken") String fcmToken);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE TokenEntity t SET t.refreshToken = :refreshToken WHERE t.userId = :userId")
    void updateRefreshTokenByUserId(@Param("userId") int userId, @Param("refreshToken") String refreshToken);

    boolean existsByUserId(int userId);
}
