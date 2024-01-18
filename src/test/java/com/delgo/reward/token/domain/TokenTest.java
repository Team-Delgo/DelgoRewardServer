package com.delgo.reward.token.domain;

import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.comm.security.jwt.JwtToken;
import com.delgo.reward.push.controller.request.FcmTokenCreate;
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
        JwtToken jwtToken = JwtToken.builder()
                .userId(1)
                .refreshToken("test token")
                .build();

        // when
        Token token = Token.from(jwtToken);

        // then
        assertThat(token.getUserId()).isEqualTo(jwtToken.getUserId());
        assertThat(token.getRefreshToken()).isEqualTo(jwtToken.getRefreshToken());
    }

    @Test
    void updateByJwt() {
        // given
        Token token = Token.builder()
                .refreshToken("test token")
                .build();
        JwtToken jwtToken = JwtToken.builder()
                .refreshToken("update token")
                .build();

        // when
        Token updatedToken = token.update(jwtToken);

        // then
        assertThat(updatedToken.getRefreshToken()).isEqualTo(jwtToken.getRefreshToken());
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