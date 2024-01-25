package com.delgo.reward.comm.push.domain;

import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.notification.domain.NotificationType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FcmMessageTest {
    @Test
    void from() {
        // given
        String token = "token";
        String title = "title";
        String body = "body";
        String image = "image url";
        String tag = "1";
        String url = "test url";

        // when
        FcmMessage fcmMessage = FcmMessage.from(token, title, body, image, tag, url);

        // then
        // AOS
        assertThat(fcmMessage.getToken()).isEqualTo(token);
        assertThat(fcmMessage.getData().getTitle()).isEqualTo("Delgo " + title);
        assertThat(fcmMessage.getData().getBody()).isEqualTo(body);
        assertThat(fcmMessage.getData().getImage()).isEqualTo(image);
        assertThat(fcmMessage.getData().getTag()).isEqualTo(tag);
        assertThat(fcmMessage.getData().getUrl()).isEqualTo(url);
        // IOS
        assertThat(fcmMessage.getApns().getPayload().getAps().getAlert().getTitle()).isEqualTo(title);
        assertThat(fcmMessage.getApns().getPayload().getAps().getAlert().getBody()).isEqualTo(body);
        assertThat(fcmMessage.getApns().getPayload().getCustom().getImageUrl()).isEqualTo(image);
        assertThat(fcmMessage.getApns().getPayload().getCustom().getUrl()).isEqualTo(url);
    }


    @Test
    void cert() {
        // given
        NotificationType notificationType = NotificationType.Comment;
        String token = "test token";
        String senderName = "test sender name";
        Certification certification = Certification.builder()
                .certificationId(1)
                .photos(List.of("test photo url"))
                .build();

        // when
        FcmMessage fcmMessage = FcmMessage.cert(notificationType, token, senderName, certification);

        // then
        // AOS
        assertThat(fcmMessage.getData().getTitle()).isEqualTo("Delgo " + notificationType.getTitle());
        assertThat(fcmMessage.getData().getBody()).isEqualTo(notificationType.getBody().apply(List.of(senderName)));
        assertThat(fcmMessage.getData().getImage()).isEqualTo(certification.getThumbnailUrl());
        assertThat(fcmMessage.getData().getTag()).isEqualTo(String.valueOf(0));
        assertThat(fcmMessage.getData().getUrl()).isEqualTo(notificationType.getUrl() + certification.getCertificationId());
        // IOS
        assertThat(fcmMessage.getApns().getPayload().getAps().getAlert().getTitle()).isEqualTo(notificationType.getTitle());
        assertThat(fcmMessage.getApns().getPayload().getAps().getAlert().getBody()).isEqualTo(notificationType.getBody().apply(List.of(senderName)));
        assertThat(fcmMessage.getApns().getPayload().getCustom().getImageUrl()).isEqualTo(certification.getThumbnailUrl());
        assertThat(fcmMessage.getApns().getPayload().getCustom().getUrl()).isEqualTo(notificationType.getUrl() + certification.getCertificationId());
    }

    @Test
    void mungple() {
        // given
        NotificationType notificationType = NotificationType.Mungple;
        String token = "test token";
        String petName = "test pet name";
        Mungple mungple = Mungple.builder()
                .mungpleId(1)
                .placeName("Test Place Name")
                .jibunAddress("서울시 송파구 동작동")
                .photoUrls(List.of("test photo url"))
                .build();

        // when
        FcmMessage fcmMessage = FcmMessage.mungple(notificationType, token, petName, mungple);

        // then
        // AOS
        assertThat(fcmMessage.getData().getTitle()).isEqualTo("Delgo " + notificationType.getTitle());
        assertThat(fcmMessage.getData().getBody()).isEqualTo(notificationType.getBody().apply(List.of(mungple.getLocalAreaName(), mungple.getPlaceName(), petName)));
        assertThat(fcmMessage.getData().getImage()).isEqualTo(mungple.getThumbnailUrl());
        assertThat(fcmMessage.getData().getTag()).isEqualTo(String.valueOf(notificationType.ordinal()));
        assertThat(fcmMessage.getData().getUrl()).isEqualTo(notificationType.getUrl() + mungple.getMungpleId());
        // IOS
        assertThat(fcmMessage.getApns().getPayload().getAps().getAlert().getTitle()).isEqualTo(notificationType.getTitle());
        assertThat(fcmMessage.getApns().getPayload().getAps().getAlert().getBody()).isEqualTo(notificationType.getBody().apply(List.of(mungple.getLocalAreaName(), mungple.getPlaceName(), petName)));
        assertThat(fcmMessage.getApns().getPayload().getCustom().getImageUrl()).isEqualTo(mungple.getThumbnailUrl());
        assertThat(fcmMessage.getApns().getPayload().getCustom().getUrl()).isEqualTo(notificationType.getUrl() + mungple.getMungpleId());
    }
}