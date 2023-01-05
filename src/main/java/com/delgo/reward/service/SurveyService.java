package com.delgo.reward.service;


import com.delgo.reward.domain.survey.Survey;
import com.delgo.reward.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public Survey register(Survey survey){
        return surveyRepository.save(survey);
    }
}
