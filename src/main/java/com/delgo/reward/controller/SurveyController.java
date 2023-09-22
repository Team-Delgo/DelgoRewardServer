package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.record.survey.SurveyRecord;
import com.delgo.reward.service.SurveyService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Hidden
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyController extends CommController {

    private final SurveyService surveyService;

    /*
     * 고객 조사 Data 저장
     * Request Data : Email
     * Response Data : null
     */
    @PostMapping
    public ResponseEntity register(@RequestBody SurveyRecord record) {
        return SuccessReturn(surveyService.register(record.toEntity()));
    }
}