package com.delgo.reward.comm.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.exception.BaseException;
import com.delgo.reward.comm.security.jwt.config.AccessTokenProperties;
import com.delgo.reward.comm.security.jwt.config.RefreshTokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
     * Header에서 X-ACCESS-TOKEN 으로 JWT 추출
     *
     * @return String
     **/
    public String getAccessToken() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader(AccessTokenProperties.HEADER_STRING).replace(AccessTokenProperties.TOKEN_PREFIX, "");
    }

    /**
     * JWT에서 userId 추출
     *
     * @return int
     * @throws BaseException
     */
    public Integer getUserId() throws BaseException {
        //1. JWT 추출
        String accessToken = getAccessToken();
        if (accessToken == null || accessToken.length() == 0) throw new BaseException(ApiCode.TOKEN_ERROR);
        // jwt 에서 userId를 추출합니다.
        int userId = Integer.parseInt(String.valueOf(JWT.require(Algorithm.HMAC512(AccessTokenProperties.SECRET)).build().verify(accessToken).getClaim("userId")));
        log.info("get Token UserId: {}", userId);
        return userId;
    }
}

