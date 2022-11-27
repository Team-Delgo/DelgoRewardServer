package com.delgo.reward.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Achievements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer achievementsId;
    private String name; // 업적 명
    private String imgUrl; // 업적 아이콘 이미지 url
    private Integer isMungple; // 업적 조건에 멍플 조건이 있는지 여부 체크

    @Transient
    private Integer isMain = 0; // 대표 이미지일 경우 순서 표시
    @Transient
    private Boolean isActive = false;

    @CreationTimestamp
    private LocalDateTime registDt; // 등록 날짜
}