package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.dto.achievements.AchievementsDTO;
import com.delgo.reward.dto.achievements.MainAchievementsDTO;
import com.delgo.reward.service.AchievementsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/achievements")
public class AchievementsController extends CommController {

    private final AchievementsService achievementsService;

    /*
     * 업적 등록
     * Request Data : AchievementsDTO, photo
     * Response Data : 등록한 업적 반환
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity register(@Validated @RequestPart(value = "data") AchievementsDTO dto, @RequestPart MultipartFile photo) {
        Achievements achievements = achievementsService.registerWithCondition(dto, photo);

        log.info("registered achievements : {}", achievements);
        return SuccessReturn(achievements);
    }

    /*
     * 업적 삭제
     * Request Data : achievementsId
     * Response Data :
     */
    @DeleteMapping(value={"/{achievementsId}",""})
    public ResponseEntity delete(@PathVariable Integer achievementsId) {
        achievementsService.delete(achievementsId);

        return SuccessReturn();
    }

    /*
     * 유저 획득 업적 리스트 조회
     * Request Data : userId
     * Response Data : 모든 업적 반환 ( But 유저 획득 업적 표시 )
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity getAchievementsByUser(@PathVariable Integer userId) {
        return SuccessReturn(achievementsService.getUserEarnedAchievements(userId));
    }

    /*
     * 대표 업적 설정 [ Deprecated ]
     * Request Data : userId, archive
     * Response Data : null
     */
    @PutMapping("/main")
    public ResponseEntity setMainAchievements(@RequestBody MainAchievementsDTO mainAchievementsDTO) {
        achievementsService.setMainAchievements(mainAchievementsDTO);
        return SuccessReturn();
    }
}