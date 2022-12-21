package com.delgo.reward.service;

import com.delgo.reward.domain.user.Token;
import com.delgo.reward.dto.user.FcmTokenDTO;
import com.delgo.reward.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public String getFcmToken(int userId){
        Token token = tokenRepository.findByUserId(userId).orElseThrow();
        return token.getFcmToken();
    }

    public boolean isFcmToken(int userId){
        return tokenRepository.findByUserId(userId).isPresent();
    }

    public void saveFcmToken(FcmTokenDTO fcmTokenDTO){
        if(isFcmToken(fcmTokenDTO.getUserId()))
            tokenRepository.updateByUserId(fcmTokenDTO.getUserId(), fcmTokenDTO.getFcmToken());
        else{
            Token token = Token.builder().userId(fcmTokenDTO.getUserId()).fcmToken(fcmTokenDTO.getFcmToken()).build();
            tokenRepository.save(token);
        }
    }

    public void deleteToken(int userId){
        tokenRepository.deleteById(userId);
    }
}
