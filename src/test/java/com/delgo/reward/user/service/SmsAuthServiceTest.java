package com.delgo.reward.user.service;

import com.delgo.reward.user.domain.SmsAuth;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SmsAuthServiceTest {
    @Autowired
    SmsAuthService smsAuthService;

    @Test
    @Transactional
    void create() {
        // given
        String phoneNo = "12345678911";
        String randNum = "1234";

        // when
        SmsAuth smsAuth = smsAuthService.create(phoneNo, randNum);

        // then
        assertThat(smsAuth.getPhoneNo()).isEqualTo(phoneNo);
        assertThat(smsAuth.getRandNum()).isEqualTo(randNum);
    }

    @Test
    @Transactional
    void update() {
        // given
        String phoneNo = "01062511583";
        String randNum = "1234";

        // when
        SmsAuth updatedSmsAuth = smsAuthService.update(phoneNo, randNum);

        // then
        assertThat(updatedSmsAuth.getPhoneNo()).isEqualTo(phoneNo);
        assertThat(updatedSmsAuth.getRandNum()).isEqualTo(randNum);
    }

    @Test
    void isExisting() {
        // given
        String phoneNo = "01062511583";

        // when
        boolean result = smsAuthService.isExisting(phoneNo);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void getOneByPhoneNo() {
        // given
        String phoneNo = "01062511583";

        // when
        SmsAuth smsAuth = smsAuthService.getOneByPhoneNo(phoneNo);

        // then
        assertThat(smsAuth.getPhoneNo()).isEqualTo(phoneNo);
    }

    @Test
    void getOneBySmsId() {
        // given
        int smsId = 66;

        // when
        SmsAuth smsAuth = smsAuthService.getOneBySmsId(smsId);

        // then
        assertThat(smsAuth.getSmsId()).isEqualTo(smsId);
    }
}