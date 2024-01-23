package com.delgo.reward.token.service;

import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.comm.security.domain.JWT;
import com.delgo.reward.token.domain.Token;
import com.delgo.reward.push.controller.request.FcmTokenCreate;
import com.delgo.reward.token.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public Token create(FcmTokenCreate fcmTokenCreate) {
        return tokenRepository.findById(fcmTokenCreate.userId())
                .map(token -> tokenRepository.save(token.update(fcmTokenCreate))) // 값이 있다면 Update
                .orElseGet(() -> tokenRepository.save(Token.from(fcmTokenCreate))); // 없다면 생성
    }

    public Token create(JWT jwt) {
        return tokenRepository.findById(jwt.userId())
                .map(token -> tokenRepository.save(token.update(jwt))) // 값이 있다면 Update
                .orElseGet(() -> tokenRepository.save(Token.from(jwt))); // 없다면 생성
    }

    public Token getOneByUserId(int userId){
        return tokenRepository.findById(userId)
                .orElseThrow(() -> new NotFoundDataException("[Token] userId : " + userId));
    }

    // token 없이도 로그아웃은 가능해야 한다.
    public void delete(int userId) {
        Token token = getOneByUserId(userId);
        tokenRepository.save(token.delete(userId));
    }
}
