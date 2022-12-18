package com.delgo.reward.domain.achievements;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;



@Getter
@Entity
@Builder
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class AchievementsCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer achievementsConditionId;
    private Integer mungpleId; // mungpleId == 0 일반 , != 0 멍플 조건
    private String categoryCode;
    private int count;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="achievementsId")
    private Achievements achievements;

    @Transient
    private boolean conditionCheck = true;

    @CreationTimestamp
    private LocalDateTime registDt; // 등록 날짜
}