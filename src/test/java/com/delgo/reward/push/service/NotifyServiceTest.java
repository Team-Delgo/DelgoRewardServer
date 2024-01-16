package com.delgo.reward.push.service;

import com.delgo.reward.push.domain.Notify;
import com.delgo.reward.push.domain.NotifyType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class NotifyServiceTest {
    @Autowired
    NotifyService notifyService;

    @Test
    @Transactional
    void create() {
        // given
        int userId = 1;
        String notifyMsg = "test msg";
        NotifyType notifyType = NotifyType.Comment;

        // when
        Notify notify = notifyService.create(userId, notifyMsg, notifyType);

        // then
        assertThat(notify.getUserId()).isEqualTo(userId);
        assertThat(notify.getNotifyMsg()).isEqualTo(notifyMsg);
        assertThat(notify.getNotifyType()).isEqualTo(notifyType);
    }

    @Test
    void getListByUserId() {
        // given
        int userId = 332;

        // when
        List<Notify> notify = notifyService.getListByUserId(userId);

        // then
        assertThat(notify.size()).isGreaterThan(0);
        assertThat(notify).extracting(Notify::getUserId).containsOnly(userId);
    }
}