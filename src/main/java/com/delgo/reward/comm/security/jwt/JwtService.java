package com.delgo.reward.comm.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.delgo.reward.comm.exception.TokenException;
import com.delgo.reward.comm.security.jwt.config.AccessTokenProperties;
import com.delgo.reward.comm.security.jwt.config.RefreshTokenProperties;
import com.delgo.reward.token.domain.Token;
import com.delgo.reward.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * JWT 토큰 서비스
 * <p>
 * jwt토큰 생성, 얻기, 인증, 검증, 토큰에서 userId 추출
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JwtService {

    private final TokenService tokenService;

    /**
     * JWT 생성
     *
     * @param userId, role
     * @return String
     **/
    // Create Token
    public JwtToken createToken(int userId) {
        return new JwtToken(
                userId,
                JWT.create() // Access Token
                        .withSubject(String.valueOf(userId))
                        .withExpiresAt(new Date(System.currentTimeMillis() + AccessTokenProperties.EXPIRATION_TIME))
                        .withClaim("userId", userId)// getUsername() == getEmail()
                        .sign(Algorithm.HMAC512(AccessTokenProperties.SECRET)),
                JWT.create() // Refresh_Token
                        .withSubject(String.valueOf(userId))
                        .withExpiresAt(new Date(System.currentTimeMillis() + RefreshTokenProperties.EXPIRATION_TIME))
                        .withClaim("userId", userId)// getUsername() == getEmail()
                        .sign(Algorithm.HMAC512(RefreshTokenProperties.SECRET))
        );
    }

    /**
     * JWT에서 userId 추출
     *
     * @return int
     */
    public Integer getUserIdByRefreshToken(String refreshToken) {
        try {
            if (!StringUtils.hasText(refreshToken))
                throw new TokenException("TOKEN IS NULL OR BLANK");

            int userId = JWT.require(Algorithm.HMAC512(RefreshTokenProperties.SECRET))
                    .build()
                    .verify(refreshToken)
                    .getClaim("userId")
                    .asInt();

            Token tokenByDatabase = tokenService.getOneByUserId(userId);
            if (!StringUtils.hasText(tokenByDatabase.getRefreshToken()) || !refreshToken.equals(tokenByDatabase.getRefreshToken()))
                throw new TokenException("TOKEN DB ERROR");

            return userId;
        } catch (TokenExpiredException e) {
            throw new TokenException("TOKEN EXPIRED");
        } catch (JWTVerificationException e) {
            throw new TokenException("TOKEN VERIFY ERROR");
        }
    }

    public HttpServletResponse publishToken(HttpServletResponse response,  JwtToken jwt) {
        // Access Token
        response.addHeader(AccessTokenProperties.HEADER_STRING, jwt.getAccessToken());

        // Refresh Token
        ResponseCookie cookie = ResponseCookie.from(RefreshTokenProperties.HEADER_STRING, jwt.getRefreshToken())
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .domain(".delgo.pet")
                .maxAge(RefreshTokenProperties.EXPIRATION_TIME)
                .build();

        // Refresh Token DB Update
        tokenService.create(jwt);

        response.addHeader("Set-Cookie", cookie.toString());
        return response;
    }
}