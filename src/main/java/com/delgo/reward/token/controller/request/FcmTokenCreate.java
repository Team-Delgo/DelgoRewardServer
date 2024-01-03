package com.delgo.reward.token.controller.request;


public record FcmTokenCreate(
        Integer userId,
        String fcmToken) {
}
