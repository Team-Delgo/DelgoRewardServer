package com.delgo.reward.domain.certification;

import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reaction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reactionId;
    private Integer certificationId;
    @Enumerated(EnumType.STRING)
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
