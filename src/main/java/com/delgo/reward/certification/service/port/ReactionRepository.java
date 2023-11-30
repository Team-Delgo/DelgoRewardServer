package com.delgo.reward.certification.service.port;

import com.delgo.reward.certification.domain.Reaction;
import com.delgo.reward.certification.domain.ReactionCode;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository {
    Reaction save(Reaction reaction);
    List<Reaction> findActiveListByCertId(Integer certificationId);
    List<Reaction> findActiveListByCertIdList(List<Integer> certIdList);
    Optional<Reaction> findByUserIdAndCertIdAndCode(Integer userId, Integer certificationId, ReactionCode reactionCode);
    Boolean existsReaction(Integer userId, Integer certificationId, ReactionCode reactionCode);
    void deleteByCertId(Integer certificationId);
}
