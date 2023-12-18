package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.repository.AchievementsRepository;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Hidden
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/achievements")
public class AchievementsController extends CommController {

    private final AchievementsRepository achievementsRepository;



    /*
     * 유저 획득 업적 리스트 조회
     * Request Data : userId
     * Response Data : 모든 업적 반환 ( But 유저 획득 업적 표시 )
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity getAchievementsByUser(@PathVariable Integer userId) {
        return SuccessReturn(achievementsRepository.findAll());
    }
}