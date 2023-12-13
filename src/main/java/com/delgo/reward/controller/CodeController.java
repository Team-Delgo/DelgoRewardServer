package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.CodeType;
import com.delgo.reward.dto.code.CodeResponse;
import com.delgo.reward.service.CodeService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
public class CodeController extends CommController {
    private final CodeService codeService;

    /***
     * GEO(지역) 코드 조회
     ***/
    @GetMapping("/geo")
    public ResponseEntity getGeoCode() {
        List<CodeResponse> response = CodeResponse.fromList(codeService.getListByType(CodeType.geo));
        return SuccessReturn(response);
    }

    /***
     * BREED(품종) 코드 조회
     ***/
    @GetMapping("/breed")
    public ResponseEntity getDogCode() {
        List<CodeResponse> response = CodeResponse.fromList(codeService.getListByType(CodeType.breed));
        return SuccessReturn(response);
    }
}