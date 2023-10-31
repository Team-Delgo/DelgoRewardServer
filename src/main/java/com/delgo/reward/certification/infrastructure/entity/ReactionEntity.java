package com.delgo.reward.certification.infrastructure.entity;

import com.delgo.reward.certification.domain.Reaction;
import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.domain.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reactionId;
    private Integer certificationId;
    @Enumerated(EnumType.STRING)
    private ReactionCode reactionCode;
    private Integer userId;
    private boolean isReaction;

    public Reaction toModel() {
        return Reaction.builder()
                .reactionId(reactionId)
                .certificationId(certificationId)
                .reactionCode(reactionCode)
                .userId(userId)
                .isReaction(isReaction)
                .build();
    }

    public static ReactionEntity from(Reaction reaction) {
        return ReactionEntity.builder()
                .certificationId(reaction.getCertificationId())
                .reactionCode(reaction.getReactionCode())
                .userId(reaction.getReactionId())
                .isReaction(reaction.getIsReaction())
                .build();
    }
}
