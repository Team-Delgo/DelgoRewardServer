package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.dto.achievements.AchievementsDTO;
import com.delgo.reward.dto.achievements.MainAchievementsDTO;
import com.delgo.reward.service.AchievementsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/achievements")
public class AchievementsController extends CommController {

    private final AchievementsService achievementsService;

    /*
     * 업적 등록
     * Request Data :
     * Response Data :
     */
    @PostMapping
    public ResponseEntity register(@RequestBody AchievementsDTO dto) {
        return SuccessReturn(achievementsService.registerWithCondition(dto));
    }

    /*
     * 유저 획득 업적 리스트 조회
     * Request Data : userId
     * Response Data : 유저획득 업적 리스트
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity getAchievementsByUser(@PathVariable Integer userId) {
        return SuccessReturn(achievementsService.getAchievementsByUser(userId));
    }

    /*
     * 대표 업적 설정
     * Request Data : userId, archive
     * Response Data : null
     */
    @PutMapping("/main")
    public ResponseEntity setMainAchievements(@RequestBody MainAchievementsDTO mainAchievementsDTO) {
        achievementsService.setMainAchievements(mainAchievementsDTO);
        return SuccessReturn();
    }
}