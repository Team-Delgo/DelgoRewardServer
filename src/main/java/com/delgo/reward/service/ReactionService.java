package com.delgo.reward.service;

import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.domain.certification.Reaction;
import com.delgo.reward.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReactionService {
    private final ReactionRepository reactionRepository;

    /**
     * [Reaction] 리액션 등록
     */
    public Reaction reaction(int userId, int certId, ReactionCode reactionCode) {
        if (hasReaction(userId, certId, reactionCode)) {
            return reactionRepository.save(getReaction(userId, certId, reactionCode).setIsReactionReverse());
        } else {
            return reactionRepository.save(Reaction.builder()
                    .userId(userId)
                    .certificationId(certId)
                    .reactionCode(reactionCode)
                    .isReaction(true)
                    .build());
        }
    }

    /**
     * [Reaction] 리액션 존재 여부 반환
     */
    public Boolean hasReaction(int userId, int certId, ReactionCode reactionCode) {
        return reactionRepository.existsByUserIdAndCertificationIdAndReactionCode(userId, certId, reactionCode);
    }

    /**
     * [Reaction] 리액션 가져오기
     */
    public Reaction getReaction(int userId, int certId, ReactionCode reactionCode) {
        return reactionRepository.findByUserIdAndCertificationIdAndReactionCode(userId, certId, reactionCode)
                .orElseThrow(() -> new NullPointerException("NOT FOUND Reaction userId : " + userId + " certificationId: " + certId + " reactionCode: " + reactionCode.getCode()));
    }

}
