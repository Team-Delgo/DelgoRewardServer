package com.delgo.reward.token.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FcmMessage {
    private boolean validateOnly;
    private Message message;

    @Getter
    @Builder
    public static class Message {
        private Notification notification;
        private String token;
    }

    @Getter
    @Builder
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}
