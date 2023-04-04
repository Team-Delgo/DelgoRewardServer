package com.delgo.reward.record.achievements;


import javax.validation.constraints.NotNull;

public record MainAchievementsRecord(
        @NotNull Integer userId,
        @NotNull Integer first,
        @NotNull Integer second,
        @NotNull Integer third) {}