package com.delgo.reward.token.service;

import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.comm.security.jwt.JwtToken;
import com.delgo.reward.push.controller.request.FcmTokenCreate;
import com.delgo.reward.token.domain.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TokenServiceTest {
    @Autowired
    TokenService tokenService;

    @Test
    @Transactional
    void createByFcm() {
        // given
        FcmTokenCreate fcmTokenCreate = FcmTokenCreate.builder()
                .userId(1)
                .fcmToken("test token")
                .build();

        // when
        Token token = tokenService.create(fcmTokenCreate);

        // then
        assertThat(token.getUserId()).isEqualTo(fcmTokenCreate.userId());
        assertThat(token.getFcmToken()).isEqualTo(fcmTokenCreate.fcmToken());
    }

    @Test
    @Transactional
    void createByJwt() {
        // given
        JwtToken jwtToken = JwtToken.builder()
                .userId(1)
                .refreshToken("test token")
                .build();

        // when
        Token token = tokenService.create(jwtToken);

        // then
        assertThat(token.getUserId()).isEqualTo(jwtToken.getUserId());
        assertThat(token.getRefreshToken()).isEqualTo(jwtToken.getRefreshToken());
    }

    @Test
    void getOneByUserId() {
        // given
        int userId = 1;

        // when
        Token token = tokenService.getOneByUserId(userId);

        // then
        assertThat(token.getUserId()).isEqualTo(userId);
    }

    @Test
    void getOneByUserId_예외처리() {
        // given
        int userId = -1;

        // when

        // then
        assertThatThrownBy(() -> {
            tokenService.getOneByUserId(userId);
        }).isInstanceOf(NotFoundDataException.class);
    }

    @Test
    @Transactional
    void delete() {
        // given
        int userId = 1;

        // when
        tokenService.delete(userId);

        // then
        Token token = tokenService.getOneByUserId(userId);
        assertThat(token.getUserId()).isEqualTo(userId);
        assertThat(token.getFcmToken()).isBlank();
        assertThat(token.getRefreshToken()).isBlank();
    }
}