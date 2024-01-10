package com.delgo.reward.token.service;

import com.delgo.reward.comm.code.ReactionCode;
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

    @Test
    void pushByComment() {
        // given
        int certificationId = 1052;
        int sendUserId = 332;

//        int receiveUserId = 332; // 창민 AOS
        int receiveUserId = 364; // 동재 AOS
//        int receiveUserId = 276; // 서은 IOS
//        int receiveUserId = 637; // 창민 IOS


        // when
        fcmService.pushByComment(sendUserId, receiveUserId, certificationId);

        // then
    }

    @Test
    void pushByReaction() {
        // given
        int certificationId = 1052;
        int sendUserId = 332;
        ReactionCode reactionCode = ReactionCode.HELPER;

//        int receiveUserId = 332; // 창민 AOS
        int receiveUserId = 364; // 동재 AOS
//        int receiveUserId = 276; // 서은 IOS
//        int receiveUserId = 637; // 창민 IOS


        // when
        fcmService.pushByReaction(sendUserId, receiveUserId, certificationId, reactionCode);

        // then
    }
}