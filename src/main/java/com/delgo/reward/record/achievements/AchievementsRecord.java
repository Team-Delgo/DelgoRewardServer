package com.delgo.reward.record.achievements;

import com.delgo.reward.domain.achievements.Achievements;

import javax.validation.constraints.NotNull;

public record AchievementsRecord(
        @NotNull String name,
        @NotNull String description,
        @NotNull Boolean isMungple,
        @NotNull String categoryCode,
        @NotNull Integer count) {
    public Achievements toEntity() {
        return Achievements.builder()
                .name(name())
                .description(description())
                .isMungple(isMungple())
                .build();
    }
}