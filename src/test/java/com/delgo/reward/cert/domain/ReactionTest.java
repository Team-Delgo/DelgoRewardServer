package com.delgo.reward.cert.domain;

import com.delgo.reward.comm.code.ReactionCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReactionTest {

    @Test
    void from() {
        // given
        int userId = 1;
        int certificationId = 2;
        ReactionCode reactionCode = ReactionCode.CUTE;
        // when
        Reaction reaction = Reaction.from(userId, certificationId, reactionCode);

        // then
        assertThat(reaction.getUserId()).isEqualTo(userId);
        assertThat(reaction.getCertificationId()).isEqualTo(certificationId);
        assertThat(reaction.getReactionCode()).isEqualTo(reactionCode);
    }

    @Test
    void update() {
        // given
        boolean isReaction = true;
        Reaction reaction = Reaction.builder()
                .isReaction(isReaction)
                .build();

        // when
        reaction.update();

        // then
        assertThat(reaction.getIsReaction()).isEqualTo(!isReaction);
    }

    @Test
    void NoArgsConstructor(){
        // given

        // when
        Reaction reaction = new Reaction();

        // then
        assertThat(reaction).isNotNull();
    }
}