package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DefaultController extends CommController {

    private final PhotoService photoService;

    @RequestMapping(value ="/")
    public ResponseEntity<?> defaultResponse() {
        return SuccessReturn();
    }

    @RequestMapping(value ="/test")
    public ResponseEntity<?> test(@RequestParam String url) {
                photoService.checkCorrectPhoto(url);

        return SuccessReturn();
    }
}
