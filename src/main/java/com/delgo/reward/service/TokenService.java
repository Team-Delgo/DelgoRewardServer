package com.delgo.reward.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.delgo.reward.comm.security.jwt.Access_JwtProperties;
import com.delgo.reward.comm.security.jwt.Refresh_JwtProperties;
import com.delgo.reward.domain.user.Token;
import com.delgo.reward.dto.user.FcmTokenDTO;
import com.delgo.reward.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public void saveFcmToken(FcmTokenDTO fcmTokenDTO){
        Token token = Token.builder().userId(fcmTokenDTO.getUserId()).fcmToken(fcmTokenDTO.getFcmToken()).build();
        tokenRepository.save(token);
    }

    // Create Token
    public String createToken(String tokenType, String email) {
        if (tokenType.equals("Access")) // Access Token
            return JWT.create()
                    .withSubject(email)
                    .withExpiresAt(new Date(System.currentTimeMillis() + Access_JwtProperties.EXPIRATION_TIME))
                    .withClaim("email", email)// getUsername() == getEmail()
                    .sign(Algorithm.HMAC512(Access_JwtProperties.SECRET));
        else // Refresh Token
            return JWT.create()
                    .withSubject(email)
                    .withExpiresAt(new Date(System.currentTimeMillis() + Refresh_JwtProperties.EXPIRATION_TIME))
                    .withClaim("email", email)// getUsername() == getEmail()
                    .sign(Algorithm.HMAC512(Refresh_JwtProperties.SECRET));
    }
}
