package com.delgo.reward.certification.service.port;

import com.delgo.reward.certification.domain.Reaction;
import com.delgo.reward.comm.code.ReactionCode;

import java.util.List;

public interface ReactionRepository {
    Reaction save(Reaction reaction);
    List<Reaction> findActiveListByCertId(Integer certificationId);
    List<Reaction> findActiveListByCertIdList(List<Integer> certIdList);
    Reaction findReaction(Integer userId, Integer certificationId, ReactionCode reactionCode);
    Boolean existsReaction(Integer userId, Integer certificationId, ReactionCode reactionCode);
    void deleteByCertId(Integer certificationId);
}
