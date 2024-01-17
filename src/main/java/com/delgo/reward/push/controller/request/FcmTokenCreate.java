package com.delgo.reward.push.controller.request;


import lombok.Builder;

@Builder
public record FcmTokenCreate(
        Integer userId,
        String fcmToken) {
}
