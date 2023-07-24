package com.delgo.reward.comm.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.comm.exception.JwtException;
import com.delgo.reward.comm.security.jwt.config.AccessTokenProperties;
import com.delgo.reward.comm.security.jwt.config.RefreshTokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * JWT 토큰 서비스
 * <p>
 * jwt토큰 생성, 얻기, 인증, 검증, 토큰에서 userId 추출
 */
@Slf4j
@Service
public class JwtService {
    /**
     * JWT 생성
     *
     * @param userId, role
     * @return String
     **/
    // Create Token
    public JwtToken createToken(int userId) {
        return new JwtToken(
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
     * @throws JwtException
     */
    public Integer getUserIdByRefreshToken(String refreshToken) throws JwtException {
        if (refreshToken == null || refreshToken.length() == 0)
            throw new JwtException(APICode.TOKEN_ERROR);

        return Integer.parseInt(
                String.valueOf(
                        JWT.require(Algorithm.HMAC512(RefreshTokenProperties.SECRET))
                                .build()
                                .verify(refreshToken) // Token 유효성 검증
                                .getClaim("userId")
                )
        );
    }
}

