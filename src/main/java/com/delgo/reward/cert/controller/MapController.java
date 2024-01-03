package com.delgo.reward.cert.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.service.MapService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/map")
public class MapController extends CommController {
    private final MapService mapService;

    @GetMapping("/{userId}")
    public ResponseEntity getMap(@PathVariable Integer userId) {
        return SuccessReturn(mapService.getMap());
    }

    @GetMapping("/other")
    public ResponseEntity getOther(@RequestParam Integer userId) {
        return SuccessReturn(mapService.getOtherMap(userId));
    }
}