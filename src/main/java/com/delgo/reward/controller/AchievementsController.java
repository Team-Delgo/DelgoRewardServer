package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.Archive;
import com.delgo.reward.dto.MainAchievementsDTO;
import com.delgo.reward.service.AchievementsService;
import com.delgo.reward.service.ArchiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        List<Archive> archives = archiveService.getArchiveByUserId(userId);
        for(Archive archive : archives)
            archive.setAchievements(achievementsService.getAchievementsById(archive.getAchievementsId()));

        return SuccessReturn(archives);
    }

    /*
     * 대표 업적 설정
     * Request Data : userId, archive
     * Response Data : 유저획득 업적 리스트
     */
    @PostMapping("/set-main")
    public ResponseEntity setMainAchievements(@RequestBody MainAchievementsDTO mainAchievementsDTO) {
        // 대표 업적 초기화
        archiveService.resetMainAchievements(mainAchievementsDTO.getUserId());

        // 사용자 지정 업적으로 대표업적 설정
        List<Archive> newMainAchievements = archiveService.setMainArchive(mainAchievementsDTO);
        for(Archive archive : newMainAchievements)
            archive.setAchievements(achievementsService.getAchievementsById(archive.getAchievementsId()));

        return SuccessReturn(newMainAchievements);
    }
}