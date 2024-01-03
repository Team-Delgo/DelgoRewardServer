package com.delgo.reward.token.domain;

import com.delgo.reward.comm.security.jwt.JwtToken;
import com.delgo.reward.common.domain.BaseTimeEntity;
import com.delgo.reward.token.controller.request.FcmTokenCreate;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token extends BaseTimeEntity {
    @Id
    private Integer userId;
    private String refreshToken;
    private String fcmToken;

    public static Token from(FcmTokenCreate fcmTokenCreate) {
        return Token.builder()
                .userId(fcmTokenCreate.userId())
                .fcmToken(fcmTokenCreate.fcmToken())
                .build();
    }

    public Token update(FcmTokenCreate fcmTokenCreate) {
        return Token.builder()
                .userId(fcmTokenCreate.userId())
                .fcmToken(fcmTokenCreate.fcmToken())
                .refreshToken(refreshToken)
                .build();
    }

    public static Token from(JwtToken jwtToken) {
        return Token.builder()
                .userId(jwtToken.getUserId())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }

    public Token update(JwtToken jwtToken) {
        return Token.builder()
                .userId(jwtToken.getUserId())
                .fcmToken(fcmToken)
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}
