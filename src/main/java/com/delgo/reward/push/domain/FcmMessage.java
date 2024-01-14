package com.delgo.reward.push.domain;

import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.mungple.domain.Mungple;
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
    public static FcmMessage cert(NotifyType notifyType, String token, String senderName, Certification certification) {
        return FcmMessage.from(
                token, // token
                notifyType.getTitle(), // title
                notifyType.getBody(List.of(senderName)), // body
                certification.getThumbnailUrl(), // image
                String.valueOf(notifyType.ordinal()), // tag
                notifyType.getUrl() + certification.getCertificationId()); // url
    }

    // 인증 관련 Push ( Mungple, MungpleByMe, MungpleByOther )
    public static FcmMessage mungple(NotifyType notifyType, String token, String petName, Mungple mungple) {
        return FcmMessage.from(
                token, // token
                notifyType.getTitle(), // title
                notifyType.getBody(List.of(mungple.getAddressForPush(), mungple.getPlaceName(), petName)), // body
                mungple.getThumbnailUrl(), // image
                String.valueOf(notifyType.ordinal()), // tag
                notifyType.getUrl() + mungple.getMungpleId()); // url
    }
}
