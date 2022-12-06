package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController extends CommController {

    private final CalendarService calendarService;

    /*
     * 날짜별 인증 조회 [ 캘린더 ]
     * Request Data : userId
     * Response Data : 캘린더 인증 리스트 반환
     */
    @GetMapping("/{userId}")
    public ResponseEntity getCalendar(@PathVariable Integer userId) {
        return SuccessReturn(calendarService.getCalendar(userId));
    }
}