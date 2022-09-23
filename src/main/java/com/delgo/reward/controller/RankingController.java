package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.dto.RankingByPointDTO;
import com.delgo.reward.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class RankingController extends CommController {
    private final RankingService rankingService;

    @GetMapping("/user")
    public ResponseEntity<?> userByPointRanking(@RequestParam @NotNull int userId){
        int userRanking = rankingService.getByPointRanking(userId);
        return SuccessReturn(userRanking);
    }

    @GetMapping("/point")
    public ResponseEntity<?> rankingByPoint(){
        rankingService.rankingByPoint();
        return SuccessReturn();
    }

    @GetMapping("/category")
    public ResponseEntity<?> rankingByCategory(@RequestParam @NotNull String categoryCode){
        rankingService.rankingByCategoryCode(categoryCode);
        return SuccessReturn();
    }

}
