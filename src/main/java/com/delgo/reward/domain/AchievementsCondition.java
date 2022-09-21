package com.delgo.reward.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementsCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer achievementsConditionId;
    private Integer achievementsId;
    private Integer mungpleId; // mungpleId == 0 일반 , != 0 멍플 조건
    private String categoryCode;
    private int count;

    @CreationTimestamp
    private LocalDateTime registDt; // 등록 날짜
}