package com.delgo.reward.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Achievements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer achievementsId;
    private String name; // 업적 명
    private String description; // 업적 설명
    private String imgUrl; // 업적 아이콘 이미지 url (대표 업적 설정되었을 때만 사용 됨)
    private Boolean isMungple; // 업적 조건에 멍플 조건이 있는지 여부 체크

    @CreationTimestamp
    private LocalDateTime registDt; // 등록 날짜

    @Transient @Builder.Default
    private Integer isMain = 0; // 대표 이미지일 경우 순서 표시
    @Transient @Builder.Default
    private Boolean isActive = false;
    @Transient @Builder.Default
    private Boolean conditionCheck = true; // 달성한 업적 체크할 때 사용함. 조회할 때는 사용 X

    public void beActive(int order) {
        this.isActive = true;
        this.isMain = order;
    }

    public void setConditionCheck(boolean check) {
        this.conditionCheck = check;
    }

    public Achievements setImg(String imgUrl) {
        this.imgUrl = imgUrl;

        return this;
    }
}