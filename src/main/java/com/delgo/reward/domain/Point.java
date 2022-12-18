package com.delgo.reward.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
