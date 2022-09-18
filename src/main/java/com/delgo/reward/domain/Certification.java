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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer certificationId;
    private Integer userId;
    private String categoryCode;

    private Integer mungpleId; // mungpleId == 0이면 mungple 장소 아님.
    private String placeName; // 장소 명
    private String description; // 내용

    private String geoCode; // 지역 코드
    private String p_geoCode; // 부모 지역 코드
    private String latitude; // 위도
    private String longitude; // 경도

    private String photoUrl; // 사진 URL
    private Integer isPhotoChecked; // 운영진 체크 여부

    @CreationTimestamp
    private LocalDateTime registDt; // 등록 날짜
}