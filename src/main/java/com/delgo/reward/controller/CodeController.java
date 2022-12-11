package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.code.Code;
import com.delgo.reward.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/code")
public class CodeController extends CommController {
    private final CodeService codeService;

    /*
     * GeoCode 전체 조회
     * Request Data : ""
     * Response Data : Geo Code List
     */
    @GetMapping("/geo")
    public ResponseEntity getGeoCode() {
        return SuccessReturn(codeService.getGeoCodeAll().stream().map(Code::formatInteger).collect(Collectors.toList()));
    }

    /*
     * DogCode 전체 조회
     * Request Data : ""
     * Response Data : Geo Code List
     */
    @GetMapping("/dog")
    public ResponseEntity getDogCode() {
        return SuccessReturn(codeService.getDogCodeAll());
    }
}