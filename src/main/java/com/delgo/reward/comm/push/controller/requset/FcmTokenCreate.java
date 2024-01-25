package com.delgo.reward.comm.push.controller.requset;


import lombok.Builder;

@Builder
public record FcmTokenCreate(
        Integer userId,
        String fcmToken) {
}
