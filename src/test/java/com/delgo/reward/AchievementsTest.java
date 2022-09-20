package com.delgo.reward;


import com.delgo.reward.domain.Achievements;
import com.delgo.reward.service.AchievementsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AchievementsTest {

    @Autowired
    private AchievementsService achievementsService;

    @Test
    public void registerCertificationTest() {
        //given
        Achievements achievements = Achievements.builder()
                .name("test")
                .imgUrl("http://test.com")
                .isMungple(0)
                .build();

        //when
        Achievements registeredAc = achievementsService.registerAchievements(achievements);

        //then
        assertNotNull(registeredAc);
    }

//    @Test
//    public void checkEarnAchievementsTest() {
//        //given
//        int userId = 0;
//        int isMungple = 0;
//
//        //when
//        List<Achievements> list = achievementsService.checkEarnAchievements(userId, isMungple);
//
//        for(Achievements achievements : list){
//            System.out.println("achievements : " + achievements);
//        }
//
//        //then
//        assertNotNull(list);
//    }
}