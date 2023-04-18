package com.delgo.reward.record.achievements;

import com.delgo.reward.domain.achievements.Achievements;

import javax.validation.constraints.NotNull;

public record AchievementsRecord(
        @NotNull String name,            // 업적 명
        @NotNull String description,     // 업적 설명
        @NotNull Boolean isMungple,      // 업적 조건에 멍플 조건이 있는지 여부 체크
        @NotNull String categoryCode,    // 업적 조건 카테고리
        @NotNull Integer count           // 얼마나 만족 해야 하는지 개수
) {
    public Achievements toEntity() {
        return Achievements.builder()
                .name(name())
                .description(description())
                .isMungple(isMungple())
                .build();
    }
}