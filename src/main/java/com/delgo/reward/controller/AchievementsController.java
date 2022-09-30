package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.Achievements;
import com.delgo.reward.domain.Archive;
import com.delgo.reward.service.AchievementsService;
import com.delgo.reward.service.ArchiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/achievements")
public class AchievementsController extends CommController {

    private final ArchiveService archiveService;
    private final AchievementsService achievementsService;

    /*
     * 유저 획득 업적 리스트 조회
     * Request Data : userId
     * Response Data : 유저획득 업적 리스트
     */
    @GetMapping("/user-data")
    public ResponseEntity getUserData(@RequestParam Integer userId) {
        List<Archive> archiveList = archiveService.getArchiveByUserId(userId);

        List<Achievements> achievementsList = new ArrayList<>();
        for(Archive archive : archiveList){
            Achievements achievements = achievementsService.getAchievementsById(archive.getAchievementsId());
            achievementsList.add(achievements);
        }

        return SuccessReturn(achievementsList);
    }
}