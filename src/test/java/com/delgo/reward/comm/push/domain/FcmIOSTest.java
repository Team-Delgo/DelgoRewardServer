package com.delgo.reward.comm.push.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class FcmIOSTest {

    @Test
    void from() {
        // given
        String title = "title";
        String body = "body";
        String image = "image url";
        String tag = "1";
        String url = "test url";

        // when
        FcmIOS fcmIos = FcmIOS.from(title, body, image, tag, url);

        // then
        assertThat(fcmIos.getPayload().getAps().getAlert().getTitle()).isEqualTo(title);
        assertThat(fcmIos.getPayload().getAps().getAlert().getBody()).isEqualTo(body);
        assertThat(fcmIos.getPayload().getCustom().getImageUrl()).isEqualTo(image);
        assertThat(fcmIos.getPayload().getCustom().getUrl()).isEqualTo(url);
        assertThat(fcmIos.getPayload().getAps().getMutableContent()).isEqualTo(1);
        assertThat(fcmIos.getPayload().getAps().getSound()).isEqualTo("default");
    }
}