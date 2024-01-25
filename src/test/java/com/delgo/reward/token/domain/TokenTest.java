package com.delgo.reward.token.domain;

import com.delgo.reward.comm.security.domain.JWT;
import com.delgo.reward.comm.push.controller.requset.FcmTokenCreate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenTest {

    @Test
    void fromByFcm() {
        // given
        FcmTokenCreate fcmTokenCreate = FcmTokenCreate.builder()
                .userId(1)
                .fcmToken("test token")
                .build();

        // when
        Token token = Token.from(fcmTokenCreate);

        // then
        assertThat(token.getUserId()).isEqualTo(fcmTokenCreate.userId());
        assertThat(token.getFcmToken()).isEqualTo(fcmTokenCreate.fcmToken());
    }

    @Test
    void updateByFcm() {
        // given
        Token token = Token.builder()
                .userId(1)
                .fcmToken("test token")
                .build();
        FcmTokenCreate fcmTokenCreate = FcmTokenCreate.builder()
                .userId(1)
                .fcmToken("update token")
                .build();

        // when
        Token updatedToken = token.update(fcmTokenCreate);

        // then
        assertThat(updatedToken.getFcmToken()).isEqualTo(fcmTokenCreate.fcmToken());
    }

    @Test
    void fromByJwt() {
        // given
        JWT jwt = JWT.builder()
                .userId(1)
                .refreshToken("test token")
                .build();

        // when
        Token token = Token.from(jwt);

        // then
        assertThat(token.getUserId()).isEqualTo(jwt.userId());
        assertThat(token.getRefreshToken()).isEqualTo(jwt.refreshToken());
    }

    @Test
    void updateByJwt() {
        // given
        Token token = Token.builder()
                .refreshToken("test token")
                .build();
        JWT jwt = JWT.builder()
                .refreshToken("update token")
                .build();

        // when
        Token updatedToken = token.update(jwt);

        // then
        assertThat(updatedToken.getRefreshToken()).isEqualTo(jwt.refreshToken());
    }

    @Test
    void delete() {
        // given
        Token token = Token.builder()
                .userId(1)
                .fcmToken("fcm token")
                .refreshToken("refresh token")
                .build();

        // when
        Token deletedToken = token.delete(token.getUserId());

        // then
        assertThat(deletedToken.getUserId()).isEqualTo(token.getUserId());
        assertThat(deletedToken.getFcmToken()).isBlank();
        assertThat(deletedToken.getRefreshToken()).isBlank();
    }

    @Test
    void NoArgsConstructor(){
        // given

        // when
        Token token = new Token();

        // then
        assertThat(token).isNotNull();
    }
}