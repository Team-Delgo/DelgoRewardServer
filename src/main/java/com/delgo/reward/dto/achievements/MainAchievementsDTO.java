package com.delgo.reward.dto.achievements;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class MainAchievementsDTO {
    @NotNull private Integer userId;
    @NotNull private Integer first; // 첫 번째 대표 업적
    @NotNull private Integer second; // 두 번째 대표 업적
    @NotNull private Integer third; // 세 번째 대표 업적
}
