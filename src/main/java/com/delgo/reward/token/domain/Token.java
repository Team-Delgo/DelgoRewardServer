package com.delgo.reward.token.domain;

import com.delgo.reward.comm.security.domain.JWT;
import com.delgo.reward.common.domain.BaseTimeEntity;
import com.delgo.reward.comm.push.controller.requset.FcmTokenCreate;
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

    public static Token from(JWT jwt) {
        return Token.builder()
                .userId(jwt.userId())
                .refreshToken(jwt.refreshToken())
                .build();
    }

    public Token update(JWT jwt) {
        return Token.builder()
                .userId(jwt.userId())
                .fcmToken(fcmToken)
                .refreshToken(jwt.refreshToken())
                .build();
    }

    public Token delete(int userId) {
        return Token.builder()
                .userId(userId)
                .fcmToken("")
                .refreshToken("")
                .build();
    }
}
