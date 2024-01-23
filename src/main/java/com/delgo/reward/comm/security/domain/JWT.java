package com.delgo.reward.comm.security.domain;

import com.auth0.jwt.algorithms.Algorithm;
import com.delgo.reward.comm.security.config.AccessTokenProperties;
import com.delgo.reward.comm.security.config.RefreshTokenProperties;
import lombok.*;

import java.util.Date;

@Builder
public record JWT(
        int userId,
        String accessToken,
        String refreshToken
) {

    public static JWT from(int userId, String ACCESS_SECRET, String REFRESH_SECRET) {
        return JWT.builder()
                .userId(userId)
                .accessToken(com.auth0.jwt.JWT.create() // Access Token
                        .withSubject(String.valueOf(userId))
                        .withExpiresAt(new Date(System.currentTimeMillis() + AccessTokenProperties.EXPIRATION_TIME))
                        .withClaim("userId", userId)// getUsername() == getEmail()
                        .sign(Algorithm.HMAC512(ACCESS_SECRET)))
                .refreshToken(com.auth0.jwt.JWT.create() // Refresh_Token
                        .withSubject(String.valueOf(userId))
                        .withExpiresAt(new Date(System.currentTimeMillis() + RefreshTokenProperties.EXPIRATION_TIME))
                        .withClaim("userId", userId)// getUsername() == getEmail()
                        .sign(Algorithm.HMAC512(REFRESH_SECRET)))
                .build();
    }
}
