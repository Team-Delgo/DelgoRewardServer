package com.delgo.reward.service;

import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.domain.certification.Reaction;
import com.delgo.reward.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        // 리액션이 존재하면 삭제
        if(hasReaction(userId, certId, reactionCode)){
            reactionRepository.deleteByUserIdAndCertificationIdAndReactionCode(userId, certId, reactionCode);
        } else {
            // 리액션이 없으면 추가
            return reactionRepository.save(Reaction.builder()
                    .userId(userId)
                    .certificationId(certId)
                    .reactionCode(reactionCode)
                    .build());
        }
        return null;
    }

    /**
     * [Reaction] 리액션 존재 여부 반환
     */
    public Boolean hasReaction(int userId, int certId, ReactionCode reactionCode) {
        return getReaction(userId, certId).getReactionCode().equals(reactionCode);
    }

    /**
     * [Reaction] 리액션 가져오기
     */
    public Reaction getReaction(int userId, int certId){
        return reactionRepository.findByUserIdAndCertificationId(userId, certId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND Reaction userId : " + userId + " certificationId: " + certId));
    }
}
