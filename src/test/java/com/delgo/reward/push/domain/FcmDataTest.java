package com.delgo.reward.push.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FcmDataTest {

    @Test
    void from() {
        // given
        String title = "title";
        String body = "body";
        String image = "image url";
        String tag = "1";
        String url = "test url";

        // when
        FcmData fcmData = FcmData.from(title, body, image, tag, url);

        // then
        assertThat(fcmData.getTitle()).isEqualTo(title);
        assertThat(fcmData.getBody()).isEqualTo(body);
        assertThat(fcmData.getImage()).isEqualTo(image);
        assertThat(fcmData.getTag()).isEqualTo(tag);
        assertThat(fcmData.getUrl()).isEqualTo(url);
    }
}