package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    // 시, 도, 광역시 기준 6개 노출(서울특별시 기준 송파구 3개 외 지역 3개)
    @GetMapping("/test")
    public ResponseEntity test(){
        return SuccessReturn(mapService.test());
    }
}