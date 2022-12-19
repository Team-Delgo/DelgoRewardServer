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
}