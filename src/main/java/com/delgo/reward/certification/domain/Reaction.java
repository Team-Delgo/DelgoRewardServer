package com.delgo.reward.certification.domain;

import com.delgo.reward.comm.code.ReactionCode;
import lombok.*;


@Getter
@Builder
public class Reaction {
    private Integer reactionId;
    private Integer certificationId;
    private ReactionCode reactionCode;
    private Integer userId;
    private boolean isReaction;

    public static Reaction from(int userId, int certId, ReactionCode reactionCode){
        return Reaction.builder()
                .userId(userId)
                .certificationId(certId)
                .reactionCode(reactionCode)
                .isReaction(true)
                .build();
    }

    public Reaction update(){
        this.isReaction = !this.isReaction;

        return this;
    }
}
