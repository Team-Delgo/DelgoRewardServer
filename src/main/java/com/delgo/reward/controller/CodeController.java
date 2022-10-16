package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.Code;
import com.delgo.reward.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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
    @GetMapping("/geo-data")
    public ResponseEntity getGeoData() {
        List<Code> codeList = codeService.getCodeAll();

        for(Code code : codeList){
            code.setN_code(Integer.parseInt(code.getCode()));
            code.setN_pCode(Integer.parseInt(code.getPCode()));
        }

        return SuccessReturn(codeList);
    }
}