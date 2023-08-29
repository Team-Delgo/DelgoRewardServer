package com.delgo.reward;

import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.mongoDomain.MongoCert;
import com.delgo.reward.mongoRepository.MongoCertRepository;
import com.delgo.reward.mongoService.MongoCertReactionService;
import com.delgo.reward.service.CertService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoCertTest {
    @Autowired
    public MongoCertReactionService mongoCertReactionService;
    @Autowired
    public MongoCertRepository mongoCertRepository;
    @Autowired
    public CertService certService;

    @Test
    public void saveMongoCertFromMariaCert() {
        Certification certification = certService.getCertById(1019);
        Map<ReactionCode, Integer> reactionCount = new HashMap<>();
        Map<ReactionCode, List<Integer>> reactionUserList = new HashMap<>();

        reactionCount.put(ReactionCode.HELPER, 0);
        reactionCount.put(ReactionCode.CUTE, 0);

        reactionUserList.put(ReactionCode.HELPER, null);
        reactionUserList.put(ReactionCode.CUTE, null);

        MongoCert mongoCert = MongoCert.builder()
                .categoryCode(certification.getCategoryCode())
                .mungpleId(certification.getMungpleId())
                .placeName(certification.getPlaceName())
                .description(certification.getDescription())
                .address(certification.getAddress())
                .geoCode(certification.getGeoCode())
                .pGeoCode(certification.getPGeoCode())
                .latitude(certification.getLatitude())
                .longitude(certification.getLongitude())
                .photoUrl(certification.getPhotoUrl())
                .isCorrect(certification.getIsCorrect())
                .isAchievements(certification.getIsAchievements())
                .commentCount(certification.getCommentCount())
                .isExpose(certification.getIsExpose())
                .reactionCount(reactionCount)
                .reactionUserList(reactionUserList)
                .build();
        mongoCertRepository.save(mongoCert);
    }

    @Test
    public void ReactionTest() {
        int userId = 333;
        String certId = "64856395125b002d1a2b48f9";
        ReactionCode reactionCode = ReactionCode.CUTE;

        MongoCert mongoCert = mongoCertReactionService.reaction(userId, certId, reactionCode);
        System.out.println(mongoCert.getReactionCount());
        System.out.println(mongoCert.getReactionUserList());
    }

}
