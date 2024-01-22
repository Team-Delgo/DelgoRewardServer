package com.delgo.reward.push.service;

import com.delgo.reward.push.domain.FcmMessage;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.service.UserQueryService;
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
    @Autowired
    UserQueryService userQueryService;

//    int receiveUserId = 332; // 창민 AOS
//    int receiveUserId = 364; // 동재 AOS
//    int receiveUserId = 276; // 서은 IOS
//    int receiveUserId = 637; // 창민 IOS

    @Test
    void push() {
        // given
        int userId = 364;
        User user = userQueryService.getOneByUserId(userId);
        String token = user.getFcmToken();
        String title = "델고는 도움이 필요해요.\uD83D\uDC40\uD83D\uDC9C";
        String body = "의견을 남겨주세요 100% 스타벅스 아메리카노를 드려요.";
        String image = "";
        String tag = "9";
        String url = "https://naver.me/5R8WYw7y";

        FcmMessage fcmMessage = FcmMessage.from(token, title, body, image, tag, url);

        // when
        fcmService.sendMessageTo(fcmMessage);

        // then
    }

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