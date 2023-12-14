package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.service.MapService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Hidden
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/map")
public class MapController extends CommController {

    private final MapService mapService;
    private final MongoMungpleService mongoMungpleService;

    /*
     * Map 멍플,인증 장소 리스트 조회 TODO: 멍플이랑 인증이랑 구분
     * Request Data : userId
     * - 멍플, 일반 인증 장소, 멍플 인증 장소 구분 필요
     * Response Data : 멍플 List, 일반 인증 List, 멍플 인증 List
     */
    @GetMapping("/{userId}")
    public ResponseEntity getMap(@PathVariable Integer userId) {
        return SuccessReturn(mapService.getMap(userId));
    }

    @GetMapping("/other")
    public ResponseEntity getOther(@RequestParam Integer userId) {
        return SuccessReturn(mapService.getOtherMap(userId));
    }

    @GetMapping("/mungple")
    public ResponseEntity getMap(@RequestParam String latitude, @RequestParam String longitude) {
        return SuccessReturn(mongoMungpleService.findWithin3Km(latitude, longitude));
    }
}