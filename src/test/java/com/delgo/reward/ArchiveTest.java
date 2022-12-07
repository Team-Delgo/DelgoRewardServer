package com.delgo.reward;


import com.delgo.reward.domain.achievements.Archive;
import com.delgo.reward.dto.achievements.MainAchievementsDTO;
import com.delgo.reward.service.ArchiveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArchiveTest {

    @Autowired
    private ArchiveService archiveService;

    @Test
    public void registerArchiveTest() {
        //given
        Archive archive = Archive.builder()
                .userId(0)
                .achievementsId(1)
                .build();

        //when
        Archive registerArchive = archiveService.register(archive);

        //then
        assertNotNull(registerArchive);
    }

    @Test
    public void getArchiveByUserIdTest() {
        //given
        int userId = 0;

        //when
        List<Archive> list = archiveService.getArchiveByUserId(userId);

        for(Archive archive : list){
            System.out.println("archive : " + archive);
        }

        //then
        assertNotNull(list);
    }

    @Test
    public void resetMainAchievementsTest() {
        //given
        int userId = 1;

        //when
        archiveService.resetMainAchievements(userId);

        //then
        assertNotNull(1);
    }

    @Test
    public void setMainAchievementsTest() {
        //given
        MainAchievementsDTO dto = new MainAchievementsDTO();
        dto.setUserId(1);
        dto.setFirst(5);
        dto.setSecond(0);
        dto.setThird(0);

        //when
        List<Archive> newMainAchievements = archiveService.setMainArchive(dto);

        for(Archive a : newMainAchievements){
            System.out.println("a : " + a);
        }

        //then
        assertNotNull(1);
    }
}