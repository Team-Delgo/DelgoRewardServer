package com.delgo.reward.record.achievements;

import javax.validation.constraints.NotNull;

public record MainAchievementsRecord (
        @NotNull Integer userId,   // 사용자 ID
        @NotNull Integer first,    // 첫 번째 대표 업적
        @NotNull Integer second,   // 두 번째 대표 업적
        @NotNull Integer third     // 세 번째 대표 업적
) {
}