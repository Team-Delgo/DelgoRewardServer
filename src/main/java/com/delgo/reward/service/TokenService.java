package com.delgo.reward.service;

import com.delgo.reward.domain.user.Token;
import com.delgo.reward.dto.FcmTokenDTO;
import com.delgo.reward.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public Optional<String> getFcmToken(int userId){
        Token token = tokenRepository.findByUserId(userId).orElse(new Token());
        return Optional.ofNullable(token.getFcmToken());
    }

    public Map<Integer, String> getFcmToken(List<Integer> userIdList){
        List<Token> tokenList = tokenRepository.findAllByUserIdIn(userIdList);
        return tokenList.stream().collect(Collectors.toMap(Token::getUserId, Token::getFcmToken));
    }

    public String getRefreshToken(int userId){
        return tokenRepository.findByUserId(userId)
                .map(Token::getRefreshToken)
                .orElse(""); // 빈 문자열 또는 다른 적절한 기본값
    }

    public boolean isToken(int userId){
        return tokenRepository.existsByUserId(userId);
    }

    public void saveFcmToken(FcmTokenDTO fcmTokenDTO){
        if(isToken(fcmTokenDTO.getUserId()))
            tokenRepository.updateFcmTokenByUserId(fcmTokenDTO.getUserId(), fcmTokenDTO.getFcmToken());
        else{
            Token token = Token.builder().userId(fcmTokenDTO.getUserId()).fcmToken(fcmTokenDTO.getFcmToken()).build();
            tokenRepository.save(token);
        }
    }

    public void saveRefreshToken(int userId, String refreshToken){
        if(isToken(userId))
            tokenRepository.updateRefreshTokenByUserId(userId, refreshToken);
        else{
            Token token = Token.builder().userId(userId).refreshToken(refreshToken).build();
            tokenRepository.save(token);
        }
    }

    // token 없이도 로그아웃은 가능해야 한다.
    public void deleteToken(int userId) {
        if (tokenRepository.findByUserId(userId).isPresent())
            tokenRepository.deleteById(userId);
    }
}
