package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DefaultController extends CommController {

    @RequestMapping(value ="/")
    public ResponseEntity<?> defaultResponse() {
        return SuccessReturn();
    }
}
