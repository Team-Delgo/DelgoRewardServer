package com.delgo.reward.certification.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ReactionTest {
    @Test
    public void from으로_Reaction을_생성할_수_있다() {
        // given
        int userId = 279;
        int certificationId = 1275;
        ReactionCode reactionCode = ReactionCode.CUTE;

        // when
        Reaction reaction = Reaction.from(userId, certificationId, reactionCode);

        // then
        assertThat(reaction.getReactionId()).isNull();
        assertThat(reaction.getCertificationId()).isEqualTo(certificationId);
        assertThat(reaction.getUserId()).isEqualTo(userId);
        assertThat(reaction.getReactionCode()).isEqualTo(reactionCode);
        assertThat(reaction.getIsReaction()).isEqualTo(true);

    }

    @Test
    public void setUrl로_URL을_변경할_수_있다() {
        // given
        int userId = 279;
        int certificationId = 1275;
        ReactionCode reactionCode = ReactionCode.CUTE;

        // when
        Reaction reaction = Reaction.from(userId, certificationId, reactionCode);
        reaction.update();

        // then
        assertThat(reaction.getIsReaction()).isEqualTo(false);
    }
}
