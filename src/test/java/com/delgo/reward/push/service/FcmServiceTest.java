package com.delgo.reward.push.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class FcmServiceTest {
    @Autowired
    FcmService fcmService;

//    int receiveUserId = 332; // 창민 AOS
//    int receiveUserId = 364; // 동재 AOS
//    int receiveUserId = 276; // 서은 IOS
//    int receiveUserId = 637; // 창민 IOS

    @Test
    void comment() {
        // given
        int sendUserId = 332;
        int receiveUserId = 364;
        int certificationId = 1200;

        // when
        fcmService.comment(sendUserId, receiveUserId, certificationId);

        // then
    }

    @Test
    void helper() {
        // given
        int sendUserId = 332;
        int receiveUserId = 364;
        int certificationId = 1200;

        // when
        fcmService.helper(sendUserId, receiveUserId, certificationId);

        // then
    }
    @Test
    void cute() {
        // given
        int sendUserId = 332;
        int receiveUserId = 364;
        int certificationId = 1200;

        // when
        fcmService.cute(sendUserId, receiveUserId, certificationId);

        // then
    }
    @Test
    void mungple() {
        // given
        int receiveUserId = 364;
        int mungpleId = 53;

        // when
        fcmService.mungple(receiveUserId, mungpleId);

        // then
    }

    @Test
    void mungpleByMe() {
        // given
        int receiveUserId = 364;
        int mungpleId = 53;

        // when
        fcmService.mungpleByMe(receiveUserId, mungpleId);

        // then
    }

    @Test
    void mungpleByOther() {
        // given
        int firstWriterId = 276;
        int receiveUserId = 364;
        int mungpleId = 53;

        // when
        fcmService.mungpleByOther(firstWriterId, receiveUserId, mungpleId);

        // then
    }

    @Test
    void birthday() {
        // given
        int receiveUserId = 364;

        // when
        fcmService.birthday(receiveUserId);

        // then
    }
}