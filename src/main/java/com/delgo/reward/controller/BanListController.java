package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.service.BanService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ban")
public class BanListController extends CommController {
    private final BanService banService;

    @PostMapping("/{userId}/{banUserId}")
    public ResponseEntity<?> ban(@PathVariable Integer userId, @PathVariable Integer banUserId){
        return SuccessReturn(banService.ban(userId, banUserId));
    }
}
