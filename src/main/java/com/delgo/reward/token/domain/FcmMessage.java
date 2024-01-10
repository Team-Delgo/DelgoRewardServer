package com.delgo.reward.token.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FcmMessage {
    private String token;
    private FcmData data; // aos
    private FcmIOS apns; // ios

    public static FcmMessage from(String token, FcmData fcmData, FcmIOS fcmIOS) {
        return FcmMessage.builder()
                .token(token)
                .data(fcmData)
                .apns(fcmIOS)
                .build();
    }
}
