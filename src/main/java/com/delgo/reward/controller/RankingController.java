package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class RankingController extends CommController {
    private final RankingService rankingService;

    @GetMapping("/point")
    public ResponseEntity<?> rankingByPoint(){
        rankingService.getByPoint();
        return SuccessReturn();
    }

}
