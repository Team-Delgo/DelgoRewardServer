package com.delgo.reward.dto.survey;

import com.delgo.reward.domain.survey.Survey;
import lombok.Getter;


@Getter
public class SurveyReqDTO {
    private String email;

    public Survey toEntity(){
        return Survey.builder()
                .email(email)
                .build();
    }
}
