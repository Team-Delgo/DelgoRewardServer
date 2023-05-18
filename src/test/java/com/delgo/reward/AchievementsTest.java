package com.delgo.reward;


import com.delgo.reward.service.AchievementsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AchievementsTest {

    @Autowired
    private AchievementsService achievementsService;


//    @Test
//    public void checkEarnAchievementsTest() {
//        //given
//        int userId = 91;
//        int isMungple = 0;
//
//        //when
//        List<Achievements> list = achievementsService.checkEarnAchievements(userId, false);
////
//        for(Achievements achievements : list){
//            System.out.println("achievements : " + achievements);
//        }
//
////        rankingService.rankingByPoint();
//
//        //then
//        assertNotNull(1);
//    }

    @Test
    public void getAchievementsByUserTest() {
        //given
        int userId = 91;
        int isMungple = 0;

        //when
//        List<Achievements> list = achievementsService.getAchievementsByUser(userId);
//
//        for(Achievements achievements : list){
//            System.out.println("achievements : " + achievements);
//        }

//        rankingService.rankingByPoint();

        //then
        assertNotNull(1);
    }
}