package com.delgo.reward.push.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NotifyTest {

    @Test
    void from() {
        // given
        int userId = 1;
        String notifyMsg = "test msg";
        NotifyType notifyType = NotifyType.Comment;
        LocalDateTime createAt = LocalDateTime.now();

        // when
        Notify notify = Notify.from(userId, notifyMsg, notifyType, createAt);

        // then
        assertThat(notify.getUserId()).isEqualTo(userId);
        assertThat(notify.getNotifyMsg()).isEqualTo(notifyMsg);
        assertThat(notify.getNotifyType()).isEqualTo(notifyType);
        assertThat(notify.getCreateAt()).isEqualTo(createAt);
    }

    @Test
    void NoArgsConstructor(){
        // given

        // when
        Notify notify = new Notify();

        // then
        assertThat(notify).isNotNull();
    }
}