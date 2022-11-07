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
public class WardOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wardOfficeId;
    private String geoCode; // 지역코드
    private String name; // 구청명
    private String latitude; // 위도
    private String longitude; // 경도

    @CreationTimestamp
    private LocalDateTime registDt;
}