package com.delgo.reward.record.survey;

import com.delgo.reward.domain.survey.Survey;

public record SurveyRecord(String email) {
    public Survey toEntity(){
        return Survey.builder()
                .email(email)
                .build();
    }
}
