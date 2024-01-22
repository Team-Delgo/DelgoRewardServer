package com.delgo.reward.user.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SmsAuthTest {

    @Test
    void from() {
        // given
        String phoneNo = "12345678911";
        String randNum = "1234";

        // when
        SmsAuth smsAuth = SmsAuth.from(phoneNo, randNum);

        // then
        assertThat(smsAuth.getPhoneNo()).isEqualTo(phoneNo);
        assertThat(smsAuth.getRandNum()).isEqualTo(randNum);
    }

    @Test
    void update() {
        // given
        SmsAuth smsAuth = SmsAuth.builder()
                .smsId(1)
                .phoneNo("test phone no")
                .randNum("1234")
                .authTime(LocalDateTime.now().minusHours(1))
                .build();
        String randNum = "4567";

        // when
        SmsAuth updatedSmsAuth = smsAuth.update(randNum);

        // then
        assertThat(updatedSmsAuth.getSmsId()).isEqualTo(smsAuth.getSmsId());
        assertThat(updatedSmsAuth.getPhoneNo()).isEqualTo(smsAuth.getPhoneNo());
        assertThat(updatedSmsAuth.getRandNum()).isEqualTo(randNum);
        assertThat(updatedSmsAuth.getAuthTime()).isAfter(smsAuth.getAuthTime());
    }

    @Test
    void isRandNumEqual() {
        // given
        SmsAuth smsAuth = SmsAuth.builder().randNum("1234").build();
        String enterNum = "1234";

        // when
        boolean result = smsAuth.isRandNumEqual(enterNum);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void isRandNumEqual_실패() {
        // given
        SmsAuth smsAuth = SmsAuth.builder().randNum("1234").build();
        String enterNum = "4567";

        // when
        boolean result = smsAuth.isRandNumEqual(enterNum);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void isAuthTimeValid() {
        // given
        LocalDateTime authTime = LocalDateTime.now();
        SmsAuth smsAuth = SmsAuth.builder().authTime(authTime).build();

        // when
        boolean result = smsAuth.isAuthTimeValid();

        // then
        assertThat(result).isTrue();
    }

    @Test
    void isAuthTimeValid_실패() {
        // given
        LocalDateTime authTime = LocalDateTime.now().minusHours(1);
        SmsAuth smsAuth = SmsAuth.builder().authTime(authTime).build();

        // when
        boolean result = smsAuth.isAuthTimeValid();

        // then
        assertThat(result).isFalse();
    }

    @Test
    void NoArgsConstructor(){
        // given

        // when
        SmsAuth smsAuth = new SmsAuth();

        // then
        assertThat(smsAuth).isNotNull();
    }
}