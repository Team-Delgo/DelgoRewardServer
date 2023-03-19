package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notify")
public class NotifyController extends CommController {
    private final NotifyService notifyService;

    /**
     * 특정 유저의 모든 알림 내역 반환
     * @param userId
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllNotifyByUserId(@RequestParam int userId){
        return SuccessReturn(notifyService.getAllNotifyByUserId(userId));
    }
}