package com.delgo.reward.mongoDomain;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.LocalDateTime;


@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="mungple")
public class MongoMungple {
    @Id private String id;
    @Field("category_code")private String categoryCode; // 카테고리 코드 ( ex. 카페, 음식점 .. )

    @Field("place_name")private String placeName;
    @Field("place_name_en")private String placeNameEn;
    @Field("road_address")private String roadAddress; // 도로명 주소
    @Field("jibun_address")private String jibunAddress; // 지번 주소

    @Field("geo_code")private String geoCode; // 지역 코드
    @Field("p_geo_code")private String pGeoCode; // 부모 지역 코드
    @Field("latitude")private String latitude; // 위도
    @Field("longitude")private String longitude; // 경도

    @Field("photo_url")private String photoUrl; // 사진 url
    @Field("detail_url")private String detailUrl; // 상세 페이지 url

    private boolean isActive; // 활성화 여부

    @Field("created_at") private LocalDateTime createdAt;
}