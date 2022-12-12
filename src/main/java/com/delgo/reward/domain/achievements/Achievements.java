package com.delgo.reward.domain.achievements;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@ToString
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

    @Transient
    @Builder.Default
    private Integer isMain = 0; // 대표 이미지일 경우 순서 표시
    @Transient
    @Builder.Default
    private Boolean isActive = false;

    @CreationTimestamp
    private LocalDateTime registDt; // 등록 날짜

    public void beActive(int order) {
        this.isActive = true;
        this.isMain = order;
    }
}