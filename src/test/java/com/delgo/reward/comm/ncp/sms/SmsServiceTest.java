package com.delgo.reward.comm.ncp.sms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class SmsServiceTest {
    @Autowired
    SmsService smsService;

    @Test
    void sendSMS() {
        // given
        String receiverNum ="01062511583";
        String msg = "[Delgo] 인증번호 " + 123;

        // when
        smsService.send(receiverNum, msg);

        // then
    }
}