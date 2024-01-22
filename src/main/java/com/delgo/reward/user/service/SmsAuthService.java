package com.delgo.reward.user.service;


import com.delgo.reward.common.service.CommService;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.user.domain.SmsAuth;
import com.delgo.reward.user.repository.SmsAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class SmsAuthService extends CommService {
    private final SmsAuthRepository smsAuthRepository;

    @Transactional
    public SmsAuth create(String phoneNo, String randNum) {
        return smsAuthRepository.save(SmsAuth.from(phoneNo, randNum));
    }

    @Transactional
    public SmsAuth update(String phoneNo, String randNum) {
        SmsAuth smsAuth = getOneByPhoneNo(phoneNo);
        return smsAuthRepository.save(smsAuth.update(randNum));
    }

    public boolean isExisting(String phoneNo) {
        return smsAuthRepository.findByPhoneNo(phoneNo).isPresent();
    }

    public SmsAuth getOneByPhoneNo(String phoneNO) {
        return smsAuthRepository.findByPhoneNo(phoneNO)
                .orElseThrow(() -> new NotFoundDataException("[SmsAuth] phoneNO : " + phoneNO));
    }

    public SmsAuth getOneBySmsId(int smsId) {
        return smsAuthRepository.findBySmsId(smsId)
                .orElseThrow(() -> new NotFoundDataException("[SmsAuth] smsId : " + smsId));
    }
}
