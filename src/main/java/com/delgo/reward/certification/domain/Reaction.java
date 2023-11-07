package com.delgo.reward.certification.domain;

import lombok.*;


@Getter
@Builder
public class Reaction {
    private Integer reactionId;
    private Integer certificationId;
    private ReactionCode reactionCode;
    private Integer userId;
    private Boolean isReaction;

    public static Reaction from(int userId, int certificationId, ReactionCode reactionCode){
        return Reaction.builder()
                .userId(userId)
                .certificationId(certificationId)
                .reactionCode(reactionCode)
                .isReaction(true)
                .build();
    }

    public Reaction update(){
        this.isReaction = !this.isReaction;

        return this;
    }
}
