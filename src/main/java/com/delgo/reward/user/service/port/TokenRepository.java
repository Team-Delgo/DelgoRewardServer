package com.delgo.reward.user.service.port;

import com.delgo.reward.user.infrastructure.entity.TokenEntity;
import java.util.List;
import java.util.Optional;

public interface TokenRepository {
    Optional<TokenEntity> findByUserId(int userId);
    List<TokenEntity> findAllByUserIdIn(List<Integer> userIdList);
    void updateFcmTokenByUserId(int userId, String fcmToken);

    void updateRefreshTokenByUserId(int userId, String refreshToken);

    boolean existsByUserId(int userId);
}
