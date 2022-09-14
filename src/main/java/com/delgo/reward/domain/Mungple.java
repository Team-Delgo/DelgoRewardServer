package com.delgo.reward.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mungple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mungpleId;
    private String categoryCode; // 카테고리 코드 ( ex. 카페, 음식점 .. )

    private String placeName;
    private String address; // 주소

    private String p_geoCode; // 부모 지역 코드
    private String c_geoCode; // 자식 지역 코드
    private String latitude; // 위도
    private String longitude; // 경도

    @CreationTimestamp
    private LocalDate registDt;
}