package com.delgo.reward.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Token {
    private Integer userId;
    private String refreshToken;
    private String fcmToken;
}
