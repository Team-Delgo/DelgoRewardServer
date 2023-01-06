package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.dto.survey.SurveyReqDTO;
import com.delgo.reward.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class SurveyController extends CommController {

    private final SurveyService surveyService;

    /*
     * 고객 조사 Data 저장
     * Request Data : Email
     * Response Data : null
     */
    @PostMapping
    public ResponseEntity register(@RequestBody SurveyReqDTO reqDTO) {
        return SuccessReturn(surveyService.register(reqDTO.toEntity()));
    }
}