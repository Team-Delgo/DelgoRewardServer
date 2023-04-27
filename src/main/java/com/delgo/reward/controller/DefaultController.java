package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value ="/api")
public class DefaultController extends CommController {

    @Value("${version}")
    String version;

    @RequestMapping
    public ResponseEntity<?> defaultResponse() {
        return SuccessReturn();
    }

    @RequestMapping(value ="/")
    public ResponseEntity<?> defaultResponse2() {
        return SuccessReturn();
    }

    @GetMapping(value ="/version")
    public ResponseEntity<?> getVersion() {
        return SuccessReturn(version);
    }
}
