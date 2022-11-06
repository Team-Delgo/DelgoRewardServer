package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.service.BanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BanListController extends CommController {
    private final BanService banService;

    @GetMapping("/ban")
    public ResponseEntity<?> ban(@RequestParam Integer userId, @RequestParam Integer banUserId){
        banService.ban(userId, banUserId);
        return SuccessReturn();
    }
}
