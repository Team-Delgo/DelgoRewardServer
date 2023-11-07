package com.delgo.reward.certification.infrastructure.jpa;

import com.delgo.reward.certification.infrastructure.entity.ReactionEntity;
import com.delgo.reward.certification.domain.ReactionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionJpaRepository extends JpaRepository<ReactionEntity, Integer> {
    List<ReactionEntity> findByIsReactionAndCertificationId(boolean isReaction, Integer certificationIdList);
    List<ReactionEntity> findByIsReactionAndCertificationIdIn(boolean isReaction, List<Integer> certificationIdList);
    Optional<ReactionEntity> findByUserIdAndCertificationIdAndReactionCode(Integer userId, Integer certId, ReactionCode reactionCode);
    Boolean existsByUserIdAndCertificationIdAndReactionCode(Integer userId, Integer certificationId, ReactionCode reactionCode);
    void deleteByCertificationId(Integer certificationId);
}
