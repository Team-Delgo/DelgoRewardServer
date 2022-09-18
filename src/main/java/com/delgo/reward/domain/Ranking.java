package com.delgo.reward.domain;

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
public class Ranking {
    @Id
    private int userId;

    private String geoCode;
    private String categoryCode;
    private int ranking;
}
