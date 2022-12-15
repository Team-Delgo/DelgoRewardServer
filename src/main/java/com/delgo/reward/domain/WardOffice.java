package com.delgo.reward.domain;


import com.delgo.reward.domain.code.Code;
import com.delgo.reward.domain.common.Location;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@ToString
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

    public WardOffice toEntity(Code code, Location location){
        return WardOffice.builder()
                .name(code.getCodeName())
                .geoCode(code.getCode())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }
}