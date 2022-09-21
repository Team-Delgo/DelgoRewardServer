package com.delgo.reward;


import com.delgo.reward.domain.AchievementsCondition;
import com.delgo.reward.service.AchievementsConditionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AchievementsConditionTest {

    @Autowired
    private AchievementsConditionService achievementsConditionService;

    @Test
    public void registerAchievementsConditionTest() {
        //given
        AchievementsCondition ac = AchievementsCondition.builder()
                .achievementsId(1)
                .mungpleId(0)
                .categoryCode("CA0002")
                .count(5)
                .build();

        //when
        AchievementsCondition registeredAc = achievementsConditionService.registerAchievementsCondition(ac);

        //then
        assertNotNull(registeredAc);
    }

    @Test
    public void getConditionByAchievementsIdTest() {
        //given
        int achievementsId = 1;

        //when
        List<AchievementsCondition> list = achievementsConditionService.getConditionByAchievementsId(achievementsId);

        for(AchievementsCondition ac : list){
            System.out.println("ac : " + ac);
        }

        //then
        assertNotNull(list);
    }
}