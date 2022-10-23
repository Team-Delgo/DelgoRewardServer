package com.delgo.reward.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Point {
    @Id
    private int userId;
    @Column(nullable = false, name = "accumulated_point")
    private int accumulatedPoint;

    @Column(nullable = false, name = "weekly_point")
    private int weeklyPoint;

    @Column(nullable = false, name = "geo_code")
    private String geoCode;

    @Column(nullable = false, name = "p_geo_code")
    private String pGeoCode;

    private int lastWeeklyPoint;
}
