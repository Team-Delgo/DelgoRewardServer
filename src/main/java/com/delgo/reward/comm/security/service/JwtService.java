package com.delgo.reward.comm.security.service;


import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.delgo.reward.comm.exception.TokenException;
import com.delgo.reward.comm.security.domain.JWT;
import com.delgo.reward.comm.security.config.AccessTokenProperties;
import com.delgo.reward.comm.security.config.RefreshTokenProperties;
import com.delgo.reward.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private final TokenService tokenService;

    @Value("${jwt.secret.access}")
    String ACCESS_SECRET;
    @Value("${jwt.secret.refresh}")
    String REFRESH_SECRET;

    public void publish(HttpServletResponse response, int userId) {
        JWT jwt = JWT.from(userId, ACCESS_SECRET, REFRESH_SECRET);
        log.info("Response refreshToken  {}", jwt.refreshToken());
        // Access Token
        response.addHeader(AccessTokenProperties.HEADER_STRING, jwt.accessToken());

        // Refresh Token
        ResponseCookie cookie = ResponseCookie.from(RefreshTokenProperties.HEADER_STRING, jwt.refreshToken())
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None")
//                .domain(".delgo.pet")
                .domain("localhost")
                .maxAge(RefreshTokenProperties.EXPIRATION_TIME)
                .build();

        // Refresh Token DB Update
        tokenService.create(jwt);

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public Integer getUserIdByRefreshToken(String refreshToken) {
        try {
            if (!StringUtils.hasText(refreshToken))
                throw new TokenException("TOKEN IS NULL OR BLANK");

            int userId = com.auth0.jwt.JWT.require(Algorithm.HMAC512(REFRESH_SECRET))
                    .build()
                    .verify(refreshToken)
                    .getClaim("userId")
                    .asInt();

            String refreshTokenFromDB = tokenService.getRefreshTokenByUserId(userId);
            if (!StringUtils.hasText(refreshTokenFromDB) || !refreshToken.equals(refreshTokenFromDB))
                throw new TokenException("TOKEN DB ERROR");

            return userId;
        } catch (TokenExpiredException e) {
            throw new TokenException("TOKEN EXPIRED");
        } catch (JWTVerificationException e) {
            throw new TokenException("TOKEN VERIFY ERROR");
        }
    }
}