package com.delgo.reward.domain.mungple;


import com.delgo.reward.domain.common.BaseTimeEntity;
import com.delgo.reward.mongoDomain.MongoMungple;
import lombok.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

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
public class Mungple extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mungpleId;
    private String categoryCode; // 카테고리 코드 ( ex. 카페, 음식점 .. )

    private String phoneNo;
    private String placeName;
    private String placeNameEn;
    private String roadAddress; // 도로명 주소
    private String jibunAddress; // 지번 주소

    private String geoCode; // 지역 코드
    private String pGeoCode; // 부모 지역 코드
    private String latitude; // 위도
    private String longitude; // 경도

    private String photoUrl; // 사진 url
    private String detailUrl; // 상세 페이지 url

    private boolean isActive; // 활성화 여부

    public Mungple setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;

        return this;
    }

    public Mungple setDetailUrl(String detailUrl){
        this.detailUrl = detailUrl;

        return this;
    }

    public Mungple setPhoneNo(String phoneNo){
        this.phoneNo = phoneNo.replace("-","");

        return this;
    }

    public MongoMungple toMongo(){
        return MongoMungple.builder()
                .categoryCode(categoryCode)
                .placeName(placeName)
                .placeNameEn(placeNameEn)
                .jibunAddress(jibunAddress)
                .roadAddress(roadAddress)
                .geoCode(geoCode)
                .pGeoCode(pGeoCode)
                .longitude(longitude)
                .latitude(latitude)
                .photoUrl(photoUrl)
                .detailUrl(detailUrl)
                .createdAt(LocalDateTime.now())
                .location(new GeoJsonPoint(Double.parseDouble(longitude), Double.parseDouble(latitude)))
                .build();
    }
}