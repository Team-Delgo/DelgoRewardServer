package com.delgo.reward.push.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmIOS {
        private Payload payload;

        @Getter
        @Builder
        public static class Payload {
            private Aps aps;
            private Custom custom;

            @Getter
            @Builder
            public static class Aps {
                private Alert alert;
                private int badge;
                private String sound;
                private String category;
                private int mutableContent;
            }

            @Getter
            @Builder
            public static class Alert {
                private String title;
                private String body;
                private String launchImage;
            }

            @Getter
            @Builder
            public static class Custom {
                private String url;
                private String imageUrl;
            }

    }

    public static FcmIOS from(String title, String body, String image, String tag, String url) {
        return FcmIOS.builder()
                .payload(Payload.builder()
                        .aps(
                                Payload.Aps.builder()
                                        .alert(
                                                Payload.Alert.builder()
                                                        .title(title)
                                                        .body(body)
                                                        .launchImage(url)
                                                        .build())
                                        .badge(1)
                                        .sound("default")
                                        .category(tag)
                                        .mutableContent(1)
                                        .build())
                        .custom(
                                Payload.Custom.builder()
                                        .url(url)
                                        .imageUrl(image)
                                        .build())
                        .build()
                ).build();
    }
}
