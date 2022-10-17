package com.delgo.reward.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MainAchievementsDTO {
    private Integer userId;
    private Integer first; // 첫 번째 대표 업적
    private Integer second; // 두 번째 대표 업적
    private Integer third; // 세 번째 대표 업적
}
