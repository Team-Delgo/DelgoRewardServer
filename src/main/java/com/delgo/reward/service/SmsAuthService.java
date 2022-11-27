package com.delgo.reward.service;


import com.delgo.reward.comm.CommService;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.ncp.sms.SmsService;
import com.delgo.reward.domain.SmsAuth;
import com.delgo.reward.repository.SmsAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SmsAuthService extends CommService {
    private final SmsService smsService;
    private final SmsAuthRepository smsAuthRepository;

    public boolean isSmsAuthExisting(String phoneNo) {
        return smsAuthRepository.findByPhoneNo(phoneNo).isPresent();
    }

    // 인증번호 발송 && CREATE
    public int createSmsAuth(String phoneNo) {
        String randNum = numberGen(4, 1);
        String message = "[Delgo] 인증번호 " + randNum;
        try {
            smsService.sendSMS(phoneNo, message);
            SmsAuth smsAuth = SmsAuth.builder().randNum(randNum).phoneNo(phoneNo).build();
            smsAuthRepository.save(smsAuth);

            int smsId = smsAuth.getSmsId();
            return smsId;
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    // 인증번호 발송 && UPDATE
    public int updateSmsAuth(String phoneNo) {
        String randNum = numberGen(4, 1);
        String message = "[Delgo] 인증번호 " + randNum;
        try {
            smsService.sendSMS(phoneNo, message);
            SmsAuth smsAuth = smsAuthRepository.findByPhoneNo(phoneNo).get();
            int smsId = smsAuth.getSmsId();
            smsAuthRepository.updateBySmsId(smsId, randNum, LocalDateTime.now());

            return smsId;
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    // 인증번호 확인
    public Optional<ApiCode> checkSMS(int smsId, String enterNum) {
        Optional<SmsAuth> findSmsAuth = smsAuthRepository.findBySmsId(smsId);
        if (!findSmsAuth.get().getRandNum().equals(enterNum)) {
            log.warn("The authentication numbers do not match");
            return Optional.of(ApiCode.AUTH_DO_NOT_MATCHING);
        }
        LocalDateTime sendTime = findSmsAuth.get().getAuthTime();
        LocalDateTime authTime = LocalDateTime.now();
        Long effectTime = ChronoUnit.MINUTES.between(sendTime, authTime);
        if (effectTime > 3)
            return Optional.of(ApiCode.AUTH_DO_NOT_MATCHING);

        return Optional.empty();
    }

    public SmsAuth getSmsAuthByPhoneNo(String phoneNO) {
        return smsAuthRepository.findByPhoneNo(phoneNO)
                .orElseThrow(() -> new NullPointerException("NOT FOUND SMS AUTH DATA"));
    }

    public SmsAuth getSmsAuthBySmsId(int smsId) {
        return smsAuthRepository.findBySmsId(smsId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND SMS AUTH DATA"));
    }

    public boolean isAuth(SmsAuth smsAuth) {
        LocalDateTime sendTime = smsAuth.getAuthTime();
        LocalDateTime authTime = LocalDateTime.now();
        Long effectTime = ChronoUnit.MINUTES.between(sendTime, authTime);
        if (effectTime < 10)
            return true;

        return false;
    }
}
