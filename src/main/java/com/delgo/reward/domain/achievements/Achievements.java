package com.delgo.reward.domain.achievements;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Entity
@Builder
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class Achievements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer achievementsId;
    private String name; // 업적 명
    private String desc; // 업적 설명
    private String imgUrl; // 업적 아이콘 이미지 url (대표 업적 설정되었을 때만 사용 됨)
    private Boolean isMungple; // 업적 조건에 멍플 조건이 있는지 여부 체크

    @CreationTimestamp
    private LocalDateTime registDt; // 등록 날짜

    @JsonManagedReference
    @OneToMany(mappedBy = "achievements", cascade = CascadeType.REMOVE)
    private List<AchievementsCondition> achievementsCondition;

    @Transient
    @Builder.Default
    private Integer isMain = 0; // 대표 이미지일 경우 순서 표시
    @Transient
    @Builder.Default
    private Boolean isActive = false;
    @Transient
    @Builder.Default
    private Boolean conditionCheck = true;

    public void beActive(int order) {
        this.isActive = true;
        this.isMain = order;
    }

    public void setConditionCheck(boolean check) {
        this.conditionCheck = check;
    }
}