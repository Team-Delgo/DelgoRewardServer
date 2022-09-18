package com.delgo.reward.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RankingByPointDTO {
    int userId;
    int ranking;
}
