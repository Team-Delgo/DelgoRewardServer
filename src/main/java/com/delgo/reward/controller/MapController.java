package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController extends CommController {

    private final MapService mapService;

    /*
     * Map 멍플,인증 장소 리스트 조회
     * Request Data : userId
     * - 멍플, 일반 인증 장소, 멍플 인증 장소 구분 필요
     * Response Data : 멍플 List, 일반 인증 List, 멍플 인증 List
     */
    @GetMapping("/data")
    public ResponseEntity getData(@RequestParam Integer userId) {
        return SuccessReturn(mapService.getMapData(userId));
    }
}