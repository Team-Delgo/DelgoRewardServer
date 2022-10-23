package com.delgo.reward.domain.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RankingPoint {
    @Id
    private int userId;
    private String geoCode;
    private int ranking;
    private int weeklyPoint;
    private int lastWeeklyPoint;
}
