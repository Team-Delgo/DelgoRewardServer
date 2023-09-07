package com.delgo.reward.service;

import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.domain.certification.Reaction;
import com.delgo.reward.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Reaction> userReactionList = reactionRepository.findByCertificationId(certId);

        if (userReactionList.size() > 0){
            userReactionList.stream()
                    .filter(reaction -> reaction.getUserId() == userId)
                    .collect(Collectors.toList());

            for (Reaction reaction: userReactionList){
                if(reaction.getReactionCode().equals(reactionCode)){
                    return true;
                }
            }
        }

        return false;
    }
}
