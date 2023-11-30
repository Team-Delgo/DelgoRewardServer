package com.delgo.reward.certification.infrastructure;

import com.delgo.reward.certification.domain.Reaction;
import com.delgo.reward.certification.infrastructure.entity.ReactionEntity;
import com.delgo.reward.certification.infrastructure.jpa.ReactionJpaRepository;
import com.delgo.reward.certification.service.port.ReactionRepository;
import com.delgo.reward.certification.domain.ReactionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Optional<Reaction> findByUserIdAndCertIdAndCode(Integer userId, Integer certId, ReactionCode reactionCode) {
        return reactionJpaRepository.findByUserIdAndCertificationIdAndReactionCode(userId, certId, reactionCode).map(ReactionEntity::toModel);
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
