package com.delgo.reward.certification.infrastructure;

import com.delgo.reward.certification.domain.Reaction;
import com.delgo.reward.certification.infrastructure.entity.ReactionEntity;
import com.delgo.reward.certification.infrastructure.jpa.ReactionJpaRepository;
import com.delgo.reward.certification.service.port.ReactionRepository;
import com.delgo.reward.comm.code.ReactionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReactionRepositoryImpl implements ReactionRepository {
    private final ReactionJpaRepository reactionJpaRepository;

    @Override
    public Reaction save(Reaction reaction) {
        return reactionJpaRepository.save(ReactionEntity.from(reaction)).toModel();
    }


    @Override
    public List<Reaction> findActiveListByCertId(Integer certificationId) {
        return reactionJpaRepository.findByIsReactionAndCertificationId(true, certificationId)
                .stream().map(ReactionEntity::toModel).toList();
    }

    @Override
    public List<Reaction> findActiveListByCertIdList(List<Integer> certIdList) {
        return reactionJpaRepository.findByIsReactionAndCertificationIdIn(true, certIdList)
                .stream().map(ReactionEntity::toModel).toList();
    }


    @Override
    public Reaction findReaction(Integer userId, Integer certId, ReactionCode reactionCode) {
        ReactionEntity entity = reactionJpaRepository.findByUserIdAndCertificationIdAndReactionCode(userId, certId, reactionCode)
                .orElseThrow(() -> new NullPointerException("NOT FOUND REACTION userId: " + userId + ", certId : " + certId));

        return entity.toModel();
    }

    @Override
    public Boolean existsReaction(Integer userId, Integer certificationId, ReactionCode reactionCode) {
        return reactionJpaRepository.existsByUserIdAndCertificationIdAndReactionCode(userId, certificationId, reactionCode);
    }

    @Override
    public void deleteByCertId(Integer certificationId) {
        reactionJpaRepository.deleteByCertificationId(certificationId);
    }
}
