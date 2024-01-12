package com.delgo.reward.push.controller.request;


public record FcmTokenCreate(
        Integer userId,
        String fcmToken) {
}
