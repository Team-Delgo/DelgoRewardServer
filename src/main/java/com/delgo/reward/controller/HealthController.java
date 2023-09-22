package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@Slf4j
@RestController
public class HealthController extends CommController {

    @GetMapping("/health-check")
    public ResponseEntity healthCheck(){
        log.info("[HealthCheck]: Good");
        return SuccessReturn();
    }
}
