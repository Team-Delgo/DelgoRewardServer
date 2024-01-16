package com.delgo.reward.cert.service;

import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.domain.Reaction;
import com.delgo.reward.comm.code.ReactionCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ReactionServiceTest {
    @Autowired
    ReactionService reactionService;

    @Test
    @Transactional
    void createHelper() {
        // given
        int userId = 1;
        int certificationId = 1;
        ReactionCode reactionCode = ReactionCode.HELPER;

        // when
        Reaction reaction = reactionService.create(userId, certificationId, reactionCode);

        // then
        assertThat(reaction.getUserId()).isEqualTo(userId);
        assertThat(reaction.getCertificationId()).isEqualTo(certificationId);
        assertThat(reaction.getReactionCode()).isEqualTo(reactionCode);
    }

    @Test
    @Transactional
    void createCute() {
        // given
        int userId = 1;
        int certificationId = 1;
        ReactionCode reactionCode = ReactionCode.CUTE;

        // when
        Reaction reaction = reactionService.create(userId, certificationId, reactionCode);

        // then
        assertThat(reaction.getUserId()).isEqualTo(userId);
        assertThat(reaction.getCertificationId()).isEqualTo(certificationId);
        assertThat(reaction.getReactionCode()).isEqualTo(reactionCode);
    }


    @Test
    @Transactional
    void update() {
        // given
        int userId = 1;
        int certificationId = 1;
        ReactionCode reactionCode = ReactionCode.CUTE;

        // when
        Reaction reaction = reactionService.update(userId, certificationId, reactionCode);

        // then
        assertThat(reaction.getUserId()).isEqualTo(userId);
        assertThat(reaction.getCertificationId()).isEqualTo(certificationId);
        assertThat(reaction.getReactionCode()).isEqualTo(reactionCode);

    }

    @Test
    void getByUserIdAndCertIdAndCode() {
        // given
        int userId = 1;
        int certificationId = 1;
        ReactionCode reactionCode = ReactionCode.CUTE;

        // when
        Reaction reaction = reactionService.getByUserIdAndCertIdAndCode(userId, certificationId, reactionCode);

        // then
        assertThat(reaction.getUserId()).isEqualTo(userId);
        assertThat(reaction.getCertificationId()).isEqualTo(certificationId);
        assertThat(reaction.getReactionCode()).isEqualTo(reactionCode);
    }

    @Test
    void getListByCertId() {
        // given
        int certificationId = 1;

        // when
        List<Reaction> reactionList = reactionService.getListByCertId(certificationId);

        // then
        assertThat(reactionList.size()).isGreaterThan(0);
        assertThat(reactionList).extracting(Reaction::getCertificationId).containsOnly(certificationId);
    }

    @Test
    void getMapByCertList() {
        // given
        int certId1 = 1;
        int certId2 = 2;
        List<Certification> certList = List.of(
                Certification.builder().certificationId(certId1).build(),
                Certification.builder().certificationId(certId2).build());

        // when
        Map<Integer, List<Reaction>> map = reactionService.getMapByCertList(certList);

        // then
        assertThat(map.get(certId1).size()).isGreaterThan(0);
        assertThat(map.get(certId2).size()).isGreaterThan(0);
        assertThat(map.get(certId1)).extracting(Reaction::getCertificationId).containsOnly(certId1);
        assertThat(map.get(certId2)).extracting(Reaction::getCertificationId).containsOnly(certId2);
    }

    @Test
    void hasReaction() {
        // given
        int userId = 1;
        int certificationId = 1;
        ReactionCode reactionCode = ReactionCode.CUTE;

        // when
        boolean result = reactionService.hasReaction(userId, certificationId, reactionCode);

        // then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @Transactional
    void deleteByCertId() {
        // given
        int certificationId = 1;

        // when
        reactionService.deleteByCertId(certificationId);

        // then
        List<Reaction> expected = reactionService.getListByCertId(certificationId);
        assertThat(expected.size()).isEqualTo(0);
    }
}