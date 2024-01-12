package com.delgo.reward.push.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmData {
    private String title;
    private String body;
    private String image;
    private String tag;
    private String url;

    public static FcmData from(String title, String body, String image, String tag, String url) {
        return FcmData.builder()
                .title(title)
                .body(body)
                .image(image)
                .tag(tag)
                .url(url)
                .build();
    }
}