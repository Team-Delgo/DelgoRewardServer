package com.delgo.reward.domain.certification;

import com.delgo.reward.comm.code.ReactionCode;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reactionId;
    private Integer certificationId;
    @Enumerated(EnumType.STRING)
    private ReactionCode reactionCode;
    private Integer userId;
    private boolean isReaction;

    public Reaction setIsReactionReverse(){
        this.isReaction = !this.isReaction;
        return this;
    }
}
