package com.delgo.reward.certification.domain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class ReactionCodeTest {

    @Test
    public void ReactionCode의_code_desc_를_호출할_수_있다(){
        // then
        assertThat(ReactionCode.HELPER.getCode()).isEqualTo("HELPER");
        assertThat(ReactionCode.HELPER.getDesc()).isEqualTo("도움돼요");
        assertThat(ReactionCode.CUTE.getCode()).isEqualTo("CUTE");
        assertThat(ReactionCode.CUTE.getDesc()).isEqualTo("귀여워요");
    }

    @Test
    public void initializeReactionMap으로_ReactionMap을_초기화할_수_있다() {
        // given
        // when
        Map<ReactionCode, Boolean> reactionMap = ReactionCode.initializeReactionMap();

        // then
        assertThat(reactionMap.get(ReactionCode.CUTE)).isEqualTo(false);
        assertThat(reactionMap.get(ReactionCode.HELPER)).isEqualTo(false);
    }

    @Test
    public void initializeReactionCountMap으로_CountMap을_초기화할_수_있다() {
        // given
        // when
        Map<ReactionCode, Integer> reactionCountMap = ReactionCode.initializeReactionCountMap();

        // then
        assertThat(reactionCountMap.get(ReactionCode.CUTE)).isEqualTo(0);
        assertThat(reactionCountMap.get(ReactionCode.HELPER)).isEqualTo(0);
    }

    @Test
    public void setReactionMapByUserId로_특정유저가_해당_게시글에_반응했는지_체크할_수_있다() {
        // given
        int userId = 1;
        List<Reaction> reactionList = new ArrayList<>();
        reactionList.add(Reaction.from(1,1,ReactionCode.CUTE));

        // when
        Map<ReactionCode, Boolean> reactionMap = ReactionCode.initializeReactionMap();
        ReactionCode.setReactionMapByUserId(reactionMap, reactionList, userId);

        // then
        assertThat(reactionMap.get(ReactionCode.CUTE)).isEqualTo(true);
        assertThat(reactionMap.get(ReactionCode.HELPER)).isEqualTo(false);
    }

    @Test
    public void setReactionCountMap로_리액션별_개수를_계산할_수_있다() {
        // given
        List<Reaction> reactionList = new ArrayList<>();
        reactionList.add(Reaction.from(1,1,ReactionCode.CUTE));
        reactionList.add(Reaction.from(1,1,ReactionCode.HELPER));
        reactionList.add(Reaction.from(2,1,ReactionCode.CUTE));

        // when
        Map<ReactionCode, Integer> countMap = ReactionCode.initializeReactionCountMap();
        ReactionCode.setReactionCountMap(countMap, reactionList);

        // then
        assertThat(countMap.get(ReactionCode.CUTE)).isEqualTo(2);
        assertThat(countMap.get(ReactionCode.HELPER)).isEqualTo(1);
    }

    @Test
    public void from으로_ENUM_value값을_가져올_수_있다() {
        // given
        String keyword = "CUTE";

        // when
       ReactionCode reactionCode = ReactionCode.from(keyword);

        // then
        assertThat(reactionCode).isEqualTo(ReactionCode.CUTE);
    }
}
