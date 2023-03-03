package com.delgo.reward.dto.achievements;

import com.delgo.reward.domain.achievements.Achievements;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AchievementsDTO {
    private String name; // 업적 명
    private String description; // 업적 설명
    private Boolean isMungple; // 업적 조건에 멍플 조건이 있는지 여부 체크
    private String categoryCode; // 업적 조건 카테고리
    private Integer count; // 얼마나 만족 해야 하는지 개수

    public Achievements toEntity(){
        return Achievements.builder()
                .name(name)
                .description(description)
                .isMungple(isMungple)
                .build();
    }
}