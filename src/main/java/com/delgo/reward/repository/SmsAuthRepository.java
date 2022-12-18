package com.delgo.reward.repository;


import com.delgo.reward.domain.SmsAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsAuthRepository extends JpaRepository<SmsAuth, Integer> {
    Optional<SmsAuth> findBySmsId(int smsId);

    Optional<SmsAuth> findByPhoneNo(String phoneNo);
}
