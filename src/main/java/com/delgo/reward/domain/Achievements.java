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
public class Achievements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer achievementsId;
    private String name; // 업적 명
    private String imgUrl; // 업적 아이콘 이미지 url
    private int isMungple; // 업적 조건에 멍플 조건이 있는지 여부 체크

    @CreationTimestamp
    private LocalDateTime registDt; // 등록 날짜
}