package com.delgo.reward.repository;


import com.delgo.reward.domain.SmsAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SmsAuthRepository extends JpaRepository<SmsAuth, Integer> {
    Optional<SmsAuth> findBySmsId(int smsId);

    Optional<SmsAuth> findByPhoneNo(String phoneNo);

    @Modifying(clearAutomatically = true)
    @Query(value = "update sms_auth set rand_num = :updateRandNum, auth_time = :updateAuthTime where sms_id = :smsId", nativeQuery = true)
    void updateBySmsId(@Param(value="smsId") int smsId, @Param(value="updateRandNum") String updateRandNum, @Param(value="updateAuthTime") LocalDateTime updateAuthTime);
}
