package com.delgo.reward.token.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmData {
    private String tag;
    private String url;
    private String title;
    private String body;
    private String image;

    public static FcmData from(NotifyType notifyType, String title, String body, String url, String image) {
        return FcmData.builder()
                .url(url)
                .tag(notifyType.getTag())
                .title(title) // 유저 명
                .body(body)
                .image(image)
                .build();
    }
}