package com.delgo.reward.certification.service;

import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.domain.Reaction;
import com.delgo.reward.certification.domain.ReactionCode;
import com.delgo.reward.fake.FakeReactionRepositoryImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactionServiceTest {
    ReactionService reactionService;

    @Before
    public void init() {
        List<Reaction> initList = new ArrayList<>();
        initList.add(Reaction.from(1, 1, ReactionCode.CUTE));
        initList.add(Reaction.from(1, 1, ReactionCode.HELPER));
        initList.add(Reaction.from(1, 2, ReactionCode.CUTE));
        initList.add(Reaction.from(1, 2, ReactionCode.HELPER));
        initList.add(Reaction.from(2, 3, ReactionCode.CUTE));
        initList.add(Reaction.from(2, 3, ReactionCode.HELPER));
        initList.add(Reaction.from(2, 4, ReactionCode.CUTE));
        initList.add(Reaction.from(2, 4, ReactionCode.HELPER));

        reactionService = ReactionService.builder()
                .reactionRepository(new FakeReactionRepositoryImpl(initList))
                .build();
    }

    @Test
    public void create로_Reaction을_생성할_수_있다() {
        // given
        int userId = 1;
        int certificationId = 21;
        ReactionCode reactionCode = ReactionCode.CUTE;

        // when
        Reaction reaction = reactionService.create(userId, certificationId, reactionCode);

        // then
        assertThat(reaction.getUserId()).isEqualTo(userId);
        assertThat(reaction.getCertificationId()).isEqualTo(certificationId);
        assertThat(reaction.getReactionCode()).isEqualTo(reactionCode);
    }

    @Test
    public void update로_isReaction_값을_반전시킬_수_있다() {
        // given
        int userId = 1;
        int certificationId =  1;
        ReactionCode reactionCode = ReactionCode.CUTE;

        // when
        Reaction reaction = reactionService.update(userId, certificationId, reactionCode);

        // then
        assertThat(reaction.getUserId()).isEqualTo(userId);
        assertThat(reaction.getCertificationId()).isEqualTo(certificationId);
        assertThat(reaction.getReactionCode()).isEqualTo(reactionCode);
        assertThat(reaction.getIsReaction()).isEqualTo(false);
    }

    @Test
    public void certId로_특정_인증_Reaction_리스트를_조회할_수_있다() {
        // given
        int certificationId = 1;

        // when
        List<Reaction> reactionList = reactionService.getListByCertId(certificationId);

        // then
        assertThat(reactionList.size()).isEqualTo(2);
        assertThat(reactionList).extracting(Reaction::getIsReaction).containsOnly(true);
        assertThat(reactionList).extracting(Reaction::getCertificationId).containsOnly(certificationId);
    }

    @Test
    public void 인증리스트로_각_인증_Reaction_리스트를_Map으로_조회_할_수_있다() {
        // given
        int firstCertificationId = 1;
        int secondCertificationId = 2;
        int thirdCertificationId = 3;
        List<Certification> certList = new ArrayList<>();
        certList.add(Certification.builder().certificationId(firstCertificationId).build());
        certList.add(Certification.builder().certificationId(secondCertificationId).build());
        certList.add(Certification.builder().certificationId(thirdCertificationId).build());

        // when
        Map<Integer, List<Reaction>> map = reactionService.getMapByCertList(certList);

        // then
        assertThat(map.get(firstCertificationId).size()).isEqualTo(2);
        assertThat(map.get(secondCertificationId).size()).isEqualTo(2);
        assertThat(map.get(thirdCertificationId).size()).isEqualTo(2);
        assertThat(map.get(firstCertificationId)).extracting(Reaction::getIsReaction).containsOnly(true);
        assertThat(map.get(secondCertificationId)).extracting(Reaction::getIsReaction).containsOnly(true);
        assertThat(map.get(thirdCertificationId)).extracting(Reaction::getIsReaction).containsOnly(true);
    }

    @Test
    public void Reaction이_존재_할_경우_True를_반환한다() {
        // given
        int userId = 1;
        int certificationId =  1;
        ReactionCode reactionCode = ReactionCode.CUTE;

        // when
        Boolean result  = reactionService.hasReaction(userId, certificationId, reactionCode);

        // then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void Reaction이_존재_하지_않을_경우_False를_반환한다() {
        // given
        int userId = 1;
        int certificationId =  21;
        ReactionCode reactionCode = ReactionCode.CUTE;

        // when
        Boolean result  = reactionService.hasReaction(userId, certificationId, reactionCode);

        // then
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void 특정_인증_관련_Reaction을_삭제_할_수_있다() {
        // given
        int certificationId =  2;

        // when
        reactionService.deleteByCertId(certificationId);
        List<Reaction> reactionList = reactionService.getListByCertId(certificationId);

        //then
        assertThat(reactionList.size()).isEqualTo(0);
    }
}
