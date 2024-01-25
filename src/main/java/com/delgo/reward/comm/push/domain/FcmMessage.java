package com.delgo.reward.comm.push.domain;

import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FcmMessage {
    private String token;
    private FcmData data; // aos
    private FcmIOS apns; // ios

    public static FcmMessage from(String token, String title, String body, String image, String tag, String url) {
        return FcmMessage.builder()
                .token(token)
                .data(FcmData.from("Delgo " + title, body, image, tag, url))
                .apns(FcmIOS.from(title, body, image, tag, url))
                .build();
    }

    // 인증 관련 Push (Comment, Helper, Cute)
    public static FcmMessage cert(NotificationType notificationType, String token, String senderName, Certification certification) {
        return FcmMessage.from(
                token, // token
                notificationType.getTitle(), // title
                notificationType.getBody().apply(List.of(senderName)), // body
                certification.getThumbnailUrl(), // image
                String.valueOf(notificationType.ordinal()), // tag
                notificationType.getUrl() + certification.getCertificationId()); // url
    }

    // 인증 관련 Push ( Mungple, MungpleByMe, MungpleByOther )
    public static FcmMessage mungple(NotificationType notificationType, String token, String petName, Mungple mungple) {
        return FcmMessage.from(
                token, // token
                notificationType.getTitle(), // title
                notificationType.getBody().apply(List.of(mungple.getLocalAreaName(), mungple.getPlaceName(), petName)), // body
                mungple.getThumbnailUrl(), // image
                String.valueOf(notificationType.ordinal()), // tag
                notificationType.getUrl() + mungple.getMungpleId()); // url
    }
}
