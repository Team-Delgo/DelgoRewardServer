package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.service.VersionService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value ="/api")
public class DefaultController extends CommController {

    private final VersionService versionService;

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
        return SuccessReturn(versionService.getVersion().getVersion());
    }
}
