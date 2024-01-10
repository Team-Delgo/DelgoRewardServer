package com.delgo.reward.token.domain;

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
                private String notifyId;
            }

    }

    public static FcmIOS from(NotifyType notifyType, String title, String url, String image, int notifyId) {
        return FcmIOS.builder()
                .payload(Payload.builder()
                        .aps(
                                Payload.Aps.builder()
                                        .alert(
                                                Payload.Alert.builder()
                                                        .title(title)
                                                        .body(notifyType.getBody())
                                                        .build())
                                        .badge(1)
                                        .sound("default")
                                        .category(notifyType.getTag())
                                        .build())
                        .custom(
                                Payload.Custom.builder()
                                        .url(url)
                                        .imageUrl(image)
                                        .notifyId(String.valueOf(notifyId))
                                        .build())
                        .build()
                ).build();
    }
}
