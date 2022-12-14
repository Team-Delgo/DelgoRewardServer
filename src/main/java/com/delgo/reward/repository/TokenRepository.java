package com.delgo.reward.repository;

import com.delgo.reward.domain.user.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByUserId(int userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update token set fcm_token = :fcmToken where user_id = :userId", nativeQuery = true)
    void updateByUserId(@Param(value="userId") int userId, @Param(value="fcmToken") String fcmToken);
}
