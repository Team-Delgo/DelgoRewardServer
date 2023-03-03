package com.delgo.reward.dto.achievements;

import com.delgo.reward.domain.achievements.Achievements;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AchievementsDTO {
    @NotNull private String name; // 업적 명
    @NotNull private String description; // 업적 설명
    @NotNull private Boolean isMungple; // 업적 조건에 멍플 조건이 있는지 여부 체크
    @NotNull private String categoryCode; // 업적 조건 카테고리
    @NotNull private Integer count; // 얼마나 만족 해야 하는지 개수

    public Achievements toEntity(){
        return Achievements.builder()
                .name(name)
                .description(description)
                .isMungple(isMungple)
                .build();
    }
}